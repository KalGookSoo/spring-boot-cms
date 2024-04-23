package com.kalgooksoo.cms.user.controller;

import com.kalgooksoo.cms.user.command.CreateUserCommand;
import com.kalgooksoo.cms.user.command.SignInCommand;
import com.kalgooksoo.cms.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Tag(name = "SignController", description = "계정 생성 및 인증을 수행하는 컨트롤러")
@Controller
@RequiredArgsConstructor
public class SignController {

    private static final String SPRING_SECURITY_SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";

    private final UserService userService;  // 계정 서비스

    private final AuthenticationManager authenticationManager;

    private final MessageSource messageSource;

    @Operation(summary = "계정 인증 화면", description = "계정 인증 화면으로 이동합니다")
    @GetMapping("/sign-in")
    public String signIn(
            @Parameter(schema = @Schema(implementation = SignInCommand.class)) @ModelAttribute("command") SignInCommand command
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        return "sign-in";
    }

    @Operation(summary = "계정 인증", description = "계정을 인증합니다")
    @PostMapping("/sign-in")
    public String signIn(
            @Parameter(schema = @Schema(implementation = SignInCommand.class)) @ModelAttribute("command") @Valid SignInCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    ) {
        // validation
        if (bindingResult.hasErrors()) {
            return "sign-in";
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

        // Redirect
        SavedRequest savedRequest = (SavedRequest) httpSession.getAttribute(SPRING_SECURITY_SAVED_REQUEST);
        if (savedRequest == null) {
            return "redirect:/";
        }
        String redirectUrl = savedRequest.getRedirectUrl();
        httpSession.removeAttribute(SPRING_SECURITY_SAVED_REQUEST);
        return "redirect:" + redirectUrl;

    }

    private Authentication authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Operation(summary = "계정 생성 화면", description = "계정 생성 화면으로 이동합니다")
    @GetMapping("/sign-up")
    public String signUp(
            @Parameter(schema = @Schema(implementation = CreateUserCommand.class)) @ModelAttribute("command") CreateUserCommand command
    ) {
        return "sign-up";
    }

    @Operation(summary = "계정 생성", description = "계정을 생성합니다")
    @PostMapping("/sign-up")
    public String signUp(
            @Parameter(schema = @Schema(implementation = CreateUserCommand.class)) @ModelAttribute("command") @Valid CreateUserCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "sign-up";
        }
        try {
            userService.createUser(command);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("username", "validation.user.username.exists");
            return "sign-up";
        }
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("command.success.create", null, LocaleContextHolder.getLocale()));
        return "redirect:/sign-in";
    }

    @Operation(summary = "계정 인증 해제", description = "계정 인증을 해제합니다")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/sign-out")
    public String signOut(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("command.success.sign.out", null, LocaleContextHolder.getLocale()));
        return "redirect:/sign-in";
    }

}