package com.influans.sp.config;

import com.influans.sp.security.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Configuration
public class SecurityTestConfig {
    @Bean
    @Profile("test")
    public SecurityContext dummySecurityContext() {
        return mock(SecurityContext.class);
    }

}
