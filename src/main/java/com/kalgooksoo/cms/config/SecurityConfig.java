package com.kalgooksoo.cms.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    private static final String LOGIN_PAGE_PATH = "/sign-in";
    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";
    private static final String LOGOUT_PATH = "/sign-out";
    private static final String ACCOUNTS_PATH = "/accounts/**";
    private static final String MANAGERS_PATH = "/managers/**";
    private static final String ADMINS_PATH = "/admins/**";

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(formLogin());
        http.userDetailsService(userDetailsService());
        http.logout(this::configureLogout);
        http.authorizeHttpRequests(authorizeHttpRequests());
        return http.build();
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequests() {
        return authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                .requestMatchers(new AntPathRequestMatcher(ACCOUNTS_PATH)).authenticated()
                .requestMatchers(new AntPathRequestMatcher(MANAGERS_PATH)).hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher(ADMINS_PATH)).hasRole("ADMIN")
                .anyRequest().permitAll();
    }

    private Customizer<FormLoginConfigurer<HttpSecurity>> formLogin() {
        return formLoginConfigurer -> formLoginConfigurer
                .loginPage(LOGIN_PAGE_PATH)
                .usernameParameter(USERNAME_PARAM)
                .passwordParameter(PASSWORD_PARAM)
                .successHandler((request, response, authentication) -> response.sendRedirect("/"))
                .failureHandler((request, response, exception) -> response.sendRedirect(LOGIN_PAGE_PATH));
    }

    private void configureLogout(LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer) {
        httpSecurityLogoutConfigurer.logoutUrl(LOGOUT_PATH).permitAll();
    }
}
