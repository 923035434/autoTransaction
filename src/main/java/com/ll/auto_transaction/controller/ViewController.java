package com.ll.auto_transaction.controller;

import com.ll.auto_transaction.controller.model.CommonResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ViewController implements ErrorController {

    @RequestMapping("view/{url}.html")
    public String modules(@PathVariable("url") String url) {
        return "modules/" + url;
    }

    @RequestMapping("view/{module}/{url}.html")
    public String modules(@PathVariable("module") String module, @PathVariable("url") String url) {
        return "modules/" + module + "/" + url;
    }

    @RequestMapping("error/{url}.html")
    public String error(@PathVariable("url") String url) {
        return "common/error/" + url;
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "index"})
    public String index() {
        return "index";
    }

    @RequestMapping("404")
    public String notFound() {
        return error("404");
    }

    @RequestMapping("403")
    public String forbidden() {
        return error("403");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error")
    public Object error(HttpServletResponse resp, HttpServletRequest request) {
        // 错误处理逻辑
        if ((request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With").toString()))) {
            return CommonResult.error(request.getRequestURL().toString() + " 该接口不存在");

        } else {
            return new ModelAndView("common/error/" + resp.getStatus() + ".html");
        }
    }

}
