package com.blacknebula.scrumpoker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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
