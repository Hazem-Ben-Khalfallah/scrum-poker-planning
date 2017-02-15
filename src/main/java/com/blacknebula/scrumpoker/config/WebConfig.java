package com.blacknebula.scrumpoker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Controller
    static class Routes {

        @RequestMapping({
                "/static/**"
        })
        public String index() {
            return "forward:/index.html";
        }
    }
}