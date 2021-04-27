package com.ll.auto_transaction.controller.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.util.WebUtils;

import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SecurityContextRepositoryWrapper implements SecurityContextRepository {

    private SecurityContextRepository contextRepository;
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    public SecurityContextRepositoryWrapper(SecurityContextRepository contextRepository) {
        this.contextRepository = contextRepository;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        HttpServletResponse response = requestResponseHolder.getResponse();
        SecurityContext context = contextRepository.loadContext(requestResponseHolder);

        SaveToResponseWrapper wrappedResponse = new SaveToResponseWrapper(
                response, request, context);
        requestResponseHolder.setResponse(wrappedResponse);

        requestResponseHolder.setRequest(new SaveToRequestWrapper(
                request, wrappedResponse));

        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {

        SaveToResponseWrapper responseWrapper = WebUtils
                .getNativeResponse(response,
                        SaveToResponseWrapper.class);
        if (responseWrapper == null) {
            throw new IllegalStateException(
                    "Cannot invoke saveContext on response "
                            + response
                            + ". You must use the HttpRequestResponseHolder.response after invoking loadContext");
        }
        // saveContext() might already be called by the response wrapper
        // if something in the chain called sendError() or sendRedirect(). This ensures we
        // only call it
        // once per request.
        if (!responseWrapper.isContextSaved()) {
            responseWrapper.saveContext(context);
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return contextRepository.containsContext(request);
    }

    private static class SaveToRequestWrapper extends
            HttpServletRequestWrapper {
        private final SaveContextOnUpdateOrErrorResponseWrapper response;

        SaveToRequestWrapper(HttpServletRequest request,
                             SaveContextOnUpdateOrErrorResponseWrapper response) {
            super(request);
            this.response = response;
        }

        @Override
        public AsyncContext startAsync() {
            response.disableSaveOnResponseCommitted();
            return super.startAsync();
        }

        @Override
        public AsyncContext startAsync(ServletRequest servletRequest,
                                       ServletResponse servletResponse) throws IllegalStateException {
            response.disableSaveOnResponseCommitted();
            return super.startAsync(servletRequest, servletResponse);
        }
    }

    final class SaveToResponseWrapper extends
            SaveContextOnUpdateOrErrorResponseWrapper {

        private final HttpServletRequest request;
        private final SecurityContext contextBeforeExecution;
        private final Authentication authBeforeExecution;

        /**
         * Takes the parameters required to call <code>saveContext()</code> successfully in addition to the request and
         * the response object we are wrapping.
         *
         * @param request                            the request object (used to obtain the redis, if one exists).
         *                                           chain executed. If this is true, and the redis is found to be null,
         *                                           this indicates that it was invalidated during the request and a new
         *                                           redis will now be created.
         * @param context                            the context before the filter chain executed. The context will only
         *                                           be stored if it or its contents changed during the request.
         */
        SaveToResponseWrapper(HttpServletResponse response,
                              HttpServletRequest request,
                              SecurityContext context) {
            super(response, false);
            this.request = request;
            this.contextBeforeExecution = context;
            this.authBeforeExecution = context.getAuthentication();
        }

        /**
         * Stores the supplied security context in the redis (if available) and if it has changed since it was set at
         * the start of the request. If the AuthenticationTrustResolver identifies the current user as anonymous, then
         * the context will not be stored.
         *
         * @param context the context object obtained from the SecurityContextHolder after the request has been
         *                processed by the filter chain. SecurityContextHolder.getContext() cannot be used to obtain the
         *                context as it has already been cleared by the time this method is called.
         *
         */
        @Override
        protected void saveContext(SecurityContext context) {
            HttpServletResponse response = (HttpServletResponse)this.getResponse();

            final Authentication authentication = context.getAuthentication();
            if (authentication == null || trustResolver.isAnonymous(authentication)) {
                if (log.isDebugEnabled()) {
                    log.debug("SecurityContext is empty or not UserAuthenticationToken.");
                }

                if (authBeforeExecution != null) {
                    contextRepository.saveContext(null, request, response);
                }
                return;
            }

            if (contextChanged(context)) {
                contextRepository.saveContext(context, request, response);
            }
        }

        private boolean contextChanged(SecurityContext context) {
            return context == null || context != contextBeforeExecution
                    || context.getAuthentication() != authBeforeExecution;
        }
    }
}
