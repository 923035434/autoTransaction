package com.ll.autotransaction.controller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;

@Configuration
public class RestConfig {


    @Value("${autoTransaction.web.enableProxy}")
    private Boolean enableProxy;

    @Bean
    public RestTemplate restTemplate(){
        if(enableProxy){
            return new RestTemplate(new SimpleClientHttpRequestFactory() {{
                setProxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1088)));
            }});
        }else {
            return new RestTemplate();
        }
    }



}
