package com.kalgooksoo.cms.user.controller;

import com.kalgooksoo.cms.user.command.UpdateUserCommand;
import com.kalgooksoo.cms.user.command.UpdateUserPasswordCommand;
import com.kalgooksoo.cms.user.entity.Authority;
import com.kalgooksoo.cms.user.entity.User;
import com.kalgooksoo.cms.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Tag(name = "UserController", description = "계정 컨트롤러")
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final MessageSource messageSource;

    @SuppressWarnings("SameParameterValue")
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    @Operation(summary = "수정 화면", description = "수정 화면")
    @PreAuthorize("authentication.principal.id == #id")
    @GetMapping("/{id}/edit")
    public String getEdit(
            @PathVariable String id,
            Model model
    ) {
        User user = userService.findById(id);
        model.addAttribute("user", user);

        Set<Authority> authorities = user.getAuthorities();
        model.addAttribute("authorities", authorities);

        UpdateUserCommand command = new UpdateUserCommand(
                user.getName(),
                user.getEmail().getId(),
                user.getEmail().getDomain(),
                user.getContactNumber().getFirst(),
                user.getContactNumber().getMiddle(),
                user.getContactNumber().getLast()
        );
        model.addAttribute("command", command);
        return "users/edit";
    }

    @Operation(summary = "수정", description = "수정")
    @PreAuthorize("authentication.principal.id == #id")
    @PutMapping("/{id}")
    public String update(
            @PathVariable String id,
            @ModelAttribute("command") @Valid UpdateUserCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        userService.update(id, command);
        redirectAttributes.addFlashAttribute("message", getMessage("command.success.update", null));
        return "redirect:/users/" + id + "/edit";
    }

    @Operation(summary = "삭제", description = "삭제")
    @PreAuthorize("authentication.principal.id == #id")
    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable String id,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes
    ) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("message", getMessage("command.success.delete", null));

        // Clear SecurityContext and invalidate HttpSession
        new SecurityContextLogoutHandler().logout(request, response, null);

        // Delete remember-me cookie
        new CookieClearingLogoutHandler("remember-me").logout(request, response, null);
        return "redirect:/";
    }

    @Operation(summary = "패스워드 수정 화면", description = "패스워드 수정 화면")
    @PreAuthorize("authentication.principal.id == #id")
    @GetMapping("/{id}/edit-password")
    public String getEditPassword(
            @PathVariable String id,
            Model model
    ) {
        UpdateUserPasswordCommand command = new UpdateUserPasswordCommand(null, null);
        model.addAttribute("command", command);
        return "users/edit_password";
    }

    @Operation(summary = "패스워드 수정", description = "패스워드 수정")
    @PreAuthorize("authentication.principal.id == #id")
    @PutMapping("/{id}/password")
    public String updatePassword(
            @PathVariable String id,
            @ModelAttribute("command") @Valid UpdateUserPasswordCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            bindingResult.rejectValue("originPassword", "validation.user.password.not.equal");
            bindingResult.rejectValue("newPassword", "validation.user.password.not.equal");
            return "users/edit_password";
        }
        try {// TODO 에러 핸들링 중앙화할 것
            userService.updatePassword(id, command.originPassword(), command.newPassword());
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("originPassword", e.getMessage());
            return "users/edit_password";
        }

        redirectAttributes.addFlashAttribute("message", getMessage("command.success.update", null));
        return "redirect:/users/" + id + "/edit-password";
    }

}
