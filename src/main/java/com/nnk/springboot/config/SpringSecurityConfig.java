package com.nnk.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for setting up Spring Security within a Spring Boot application.
 * This class configures various security aspects like URL access rules, form login, logout behavior,
 * and the overall security filter chain that Spring Security uses to protect the application.
 * It leverages the {@link EnableWebSecurity} annotation to switch off the default web security configuration
 * and apply custom configurations defined in this class.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Configures the {@link SecurityFilterChain} which defines the rules and settings
     * for handling security at the HTTP level. This method sets up form login, authorization
     * rules per endpoint, logout configuration, and exception handling for access denied scenarios.
     *
     * @param http The {@link HttpSecurity} object provided by Spring Security to configure the aspects like URL security, login, logout etc.
     * @return The fully configured {@link SecurityFilterChain}.
     * @throws Exception Throws if there's an error during the configuration which could be due to security rule conflicts or misconfigurations.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(form -> form.defaultSuccessUrl("/admin/home").permitAll())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/",
                            "/admin/home",
                            "/bidList/list",
                            "/bidList/add",
                            "/bidList/validate",
                            "/bidList/update/**",
                            "/bidList/delete/**",
                            "/curvePoint/list",
                            "/curvePoint/add",
                            "/curvePoint/validate",
                            "/curvePoint/update/**",
                            "/curvePoint/delete/**",
                            "/rating/list",
                            "/rating/add",
                            "/rating/validate",
                            "/rating/update/**",
                            "/rating/delete/**",
                            "/ruleName/list",
                            "/ruleName/add",
                            "/ruleName/validate",
                            "/ruleName/update/**",
                            "/ruleName/delete/**",
                            "/trade/list",
                            "/trade/add",
                            "/trade/validate",
                            "/trade/update/**",
                            "/trade/delete/**"
                    ).hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers(
                            "/user/list",
                            "/user/add",
                            "/user/validate",
                            "/user/update/**",
                            "/user/delete/**"
                    ).hasRole("ADMIN");
                    auth.requestMatchers("/app/error").permitAll();
                    auth.anyRequest().authenticated();
                })
                .logout(logout -> logout.logoutUrl("/app-logout").logoutSuccessUrl("/login").invalidateHttpSession(true).deleteCookies("JSESSIONID"))
                .exceptionHandling((exception -> exception.accessDeniedPage("/app/error")))
                .build();
    }

    /**
     * Provides a {@link BCryptPasswordEncoder} bean to be used across the application
     * for encoding passwords securely.
     *
     * @return A new instance of {@link BCryptPasswordEncoder}.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures and provides the {@link AuthenticationManager} to manage authentication processes.
     * The method uses {@link AuthenticationManagerBuilder} to set up the service that provides user details
     * and the encoder for passwords.
     *
     * @param http The {@link HttpSecurity} object to access the shared objects needed to configure the authentication manager.
     * @param bCryptPasswordEncoder The {@link BCryptPasswordEncoder} used for encoding passwords during authentication.
     * @return The fully configured {@link AuthenticationManager}.
     * @throws Exception Throws if there's an error during the configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);

        return authenticationManagerBuilder.build();
    }
}
