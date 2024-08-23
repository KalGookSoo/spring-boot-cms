package com.kalgooksoo.cms.config;

import com.kalgooksoo.cms.security.service.JpaPersistentTokenRepository;
import com.kalgooksoo.cms.user.repository.UserPrincipalRepository;
import com.kalgooksoo.cms.user.service.DefaultUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.UUID;

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

    private final UserPrincipalRepository userPrincipalRepository;

    private final JpaPersistentTokenRepository jpaPersistentTokenRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new DefaultUserDetailsService(userPrincipalRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return jpaPersistentTokenRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(this::handleAuthorizeHttpRequests);
        http.formLogin(this::handleFormLogin);
        http.userDetailsService(userDetailsService());
        http.logout(this::configureLogout);
        http.rememberMe(this::handleRememberMe);
        return http.build();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices(
                UUID.randomUUID().toString(),
                userDetailsService(),
                persistentTokenRepository()
        );
        services.setAlwaysRemember(false);
        return services;
    }

    private void handleAuthorizeHttpRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
        // TODO /swagger resources 관련 ROLE_ADMIN만 접근할 수 있도록 할 것
        authorizationManagerRequestMatcherRegistry
                .requestMatchers(new AntPathRequestMatcher("/managers/**")).hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/admins/**")).hasRole("ADMIN")
                .anyRequest()
                .permitAll();
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

    private void handleRememberMe(RememberMeConfigurer<HttpSecurity> httpSecurityRememberMeConfigurer) {
        String key = UUID.randomUUID().toString();
        int tokenValiditySeconds = 60 * 60 * 24;
        httpSecurityRememberMeConfigurer
                .key(key)
                .rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService())
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(tokenValiditySeconds);
    }

}
