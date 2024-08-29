package com.kalgooksoo.cms.user.controller;

import com.kalgooksoo.cms.user.command.CreateUserCommand;
import com.kalgooksoo.cms.user.command.SignInCommand;
import com.kalgooksoo.cms.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final UserService userService;

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
        return authentication == null || authentication instanceof AnonymousAuthenticationToken ? "sign_in" : "redirect:/";
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

}