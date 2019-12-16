package com.blacknebula.scrumpoker.config;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    private static final String GROUP_NAME = "scrum-poker-api";

    private static final Set<String> HIDDEN_ENDPOINTS = Sets.newHashSet("/error",
            "/actuator(.*)");

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiEndPointsInfo())
                .groupName(GROUP_NAME)
                .useDefaultResponseMessages(true)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(getExcludedPaths())
                .build();
    }


    private Predicate<String> getExcludedPaths() {
        return and(HIDDEN_ENDPOINTS.stream()
                .map(path -> not(PathSelectors.regex(path)))
                .collect(Collectors.toSet()));
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Scrum poker REST API")
                .description("Please be welcome to browse our Scrum poker REST API")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }

}
