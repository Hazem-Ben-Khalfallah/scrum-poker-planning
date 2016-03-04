package com.influans.sp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Locale;

@ComponentScan(basePackages = {"com.influans.sp"})
@PropertySource("classpath:/websocket.properties")
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        new Application().configure(new SpringApplicationBuilder(Application.class)).run(args);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        Locale.setDefault(Locale.ENGLISH);
        return application.sources(Application.class);
    }

}
