package com.kalgooksoo.cms.user.controller;

import com.kalgooksoo.cms.user.command.CreateUserCommand;
import com.kalgooksoo.cms.user.command.SignInCommand;
import com.kalgooksoo.cms.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 계정 인증 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class SignController {

    private static final String SPRING_SECURITY_SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final RememberMeServices rememberMeServices;

    private final MessageSource messageSource;

    /**
     * 계정 인증 화면을 반환합니다.
     * @param command 계정 인증 커맨드
     * @return 계정 인증 화면
     */
    @GetMapping("/sign-in")
    public String signIn(
            @ModelAttribute("command") SignInCommand command
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof AnonymousAuthenticationToken ? "sign_in" : "redirect:/";
    }

    /**
     * 계정 인증 처리합니다.
     * @param command            계정 인증 커맨드
     * @param bindingResult      검증 결과
     * @param redirectAttributes 리다이렉트 속성
     * @param request            요청
     * @param response           응답
     * @return 메인 화면
     */
    @PostMapping("/sign-in")
    public String signIn(
            @ModelAttribute("command") @Valid SignInCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // validation
        if (bindingResult.hasErrors()) {
            return "sign_in";
        }

        // Authenticate
        Authentication authentication;
        try {
            authentication = authenticate(command.username(), command.password());
        } catch (AuthenticationException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/sign-in";
        }

        // Set SecurityContext & HttpSession
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        // Remember me
        rememberMeServices.loginSuccess(request, response, authentication);

        // Redirect
        SavedRequest savedRequest = (SavedRequest) httpSession.getAttribute(SPRING_SECURITY_SAVED_REQUEST);
        if (savedRequest == null) {
            return "redirect:/";
        }
        String redirectUrl = savedRequest.getRedirectUrl();
        httpSession.removeAttribute(SPRING_SECURITY_SAVED_REQUEST);
        return "redirect:" + redirectUrl;

    }

    /**
     * 계정 생성 화면을 반환합니다.
     * @param command 계정 생성 커맨드
     * @return 계정 생성 화면
     */
    @GetMapping("/sign-up")
    public String signUp(
            @ModelAttribute("command") CreateUserCommand command
    ) {
        return "sign_up";
    }

    /**
     * 계정 생성 처리합니다.
     * @param command            계정 생성 커맨드
     * @param bindingResult      검증 결과
     * @param redirectAttributes 리다이렉트 속성
     * @return 계정 인증 화면
     */
    @PostMapping("/sign-up")
    public String signUp(
            @ModelAttribute("command") @Valid CreateUserCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "sign_up";
        }
        try {
            userService.createUser(command);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("username", "validation.user.username.exists");
            return "sign_up";
        }
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("command.success.create", null, LocaleContextHolder.getLocale()));
        return "redirect:/sign-in";
    }

    /**
     * 계정 인증 해제 처리합니다.
     * @param request            요청
     * @param response           응답
     * @param redirectAttributes 리다이렉트 속성
     * @return 계정 인증 화면
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/sign-out")
    public String signOut(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        // Clear SecurityContext and invalidate HttpSession
        new SecurityContextLogoutHandler().logout(request, response, null);

        // Delete remember-me cookie
        new CookieClearingLogoutHandler("remember-me").logout(request, response, null);

        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("command.success.sign.out", null, LocaleContextHolder.getLocale()));
        return "redirect:/sign-in";
    }

    private Authentication authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

}