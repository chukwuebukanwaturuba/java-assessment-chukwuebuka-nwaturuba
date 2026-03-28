package com.example.security.autoconfigure;

import com.example.security.config.SecurityProperties;
import com.example.security.exception.GlobalSecurityExceptionHandler;
import com.example.security.exception.JwtAccessDeniedHandler;
import com.example.security.exception.JwtAuthenticationEntryPoint;
import com.example.security.filter.JwtAuthenticationFilter;
import com.example.security.filter.SecurityLoggingFilter;
import com.example.security.jwt.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

// Auto-configuration entry point for the starter. Wires up JwtTokenUtil, the JWT filter,
// the 401/403 handlers, the logging filter, and the exception advice as beans.
// The consuming app just needs to provide a UserDetailsService and configure its own SecurityFilterChain.
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenUtil jwtTokenUtil(SecurityProperties securityProperties) {
        return new JwtTokenUtil(securityProperties);
    }

    // Only created when the app provides a UserDetailsService.
    // This keeps the starter from caring about how users are stored.
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UserDetailsService.class)
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil,
                                                           UserDetailsService userDetailsService,
                                                           SecurityProperties securityProperties) {
        return new JwtAuthenticationFilter(jwtTokenUtil, userDetailsService, securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAccessDeniedHandler jwtAccessDeniedHandler(ObjectMapper objectMapper) {
        return new JwtAccessDeniedHandler(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityLoggingFilter securityLoggingFilter() {
        return new SecurityLoggingFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalSecurityExceptionHandler globalSecurityExceptionHandler() {
        return new GlobalSecurityExceptionHandler();
    }
}
