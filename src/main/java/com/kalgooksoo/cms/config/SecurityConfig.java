package com.kalgooksoo.cms.config;

import com.kalgooksoo.cms.user.repository.UserPrincipalRepository;
import com.kalgooksoo.cms.user.service.DefaultUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String LOGIN_PAGE_PATH = "/sign-in";

    private static final String USERNAME_PARAM = "username";

    private static final String PASSWORD_PARAM = "password";

    private static final String LOGOUT_PATH = "/sign-out";

    private static final String USERS_PATH = "/users/**";

    private static final String MANAGERS_PATH = "/managers/**";

    private static final String ADMINS_PATH = "/admins/**";

    private final UserPrincipalRepository userPrincipalRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new DefaultUserDetailsService(userPrincipalRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(this::handleFormLogin);
        http.userDetailsService(userDetailsService());
        http.logout(this::configureLogout);
        http.authorizeHttpRequests(this::handleAuthorizeHttpRequests);
        return http.build();
    }

    private void handleAuthorizeHttpRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
        authorizationManagerRequestMatcherRegistry
                .requestMatchers(new AntPathRequestMatcher(USERS_PATH)).authenticated()
                .requestMatchers(new AntPathRequestMatcher(MANAGERS_PATH)).hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher(ADMINS_PATH)).hasRole("ADMIN")
                .anyRequest().permitAll();
    }

    private void handleFormLogin(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {
        httpSecurityFormLoginConfigurer
                .loginPage(LOGIN_PAGE_PATH)
                .usernameParameter(USERNAME_PARAM)
                .passwordParameter(PASSWORD_PARAM)
                .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                .failureHandler(this::handleBadCredentialsException);
    }

    private void handleBadCredentialsException(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        logger.error(exception.getMessage());
        response.sendRedirect(LOGIN_PAGE_PATH);
    }

    private void configureLogout(LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer) {
        httpSecurityLogoutConfigurer
                .logoutUrl(LOGOUT_PATH)
                .permitAll();
    }

}
