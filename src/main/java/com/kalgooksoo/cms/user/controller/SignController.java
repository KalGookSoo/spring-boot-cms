package com.kalgooksoo.cms.user.controller;

import com.kalgooksoo.cms.user.command.CreateUserCommand;
import com.kalgooksoo.cms.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class SignController {

    private final UserService userService;  // 계정 서비스

    @Operation(summary = "로그인 화면", description = "로그인 화면으로 이동합니다.")
    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }

    @Operation(summary = "계정 생성 화면", description = "계정 생성 화면으로 이동합니다.")
    @GetMapping("/sign-up")
    public String signUp(
            @Parameter(schema = @Schema(implementation = CreateUserCommand.class)) @ModelAttribute("command") CreateUserCommand command
    ) {
        return "sign-up";
    }

    @Operation(summary = "계정 생성", description = "계정을 생성합니다.")
    @PostMapping("/sign-up")
    public String signUp(
            @Parameter(schema = @Schema(implementation = CreateUserCommand.class)) @ModelAttribute("command") @Valid CreateUserCommand command,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return "sign-up";
        }
        try {
            userService.createUser(command);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("username", "username.exists", "계정이 이미 존재합니다.");
            return "sign-up";
        }
        redirectAttributes.addFlashAttribute("message", "계정이 생성되었습니다.");
        return "redirect:/sign-in";
    }

}