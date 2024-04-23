package com.kalgooksoo.cms.user.controller;

import com.kalgooksoo.cms.user.command.UpdateUserCommand;
import com.kalgooksoo.cms.user.command.UpdateUserPasswordCommand;
import com.kalgooksoo.cms.user.entity.User;
import com.kalgooksoo.cms.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Tag(name = "UserController", description = "계정 컨트롤러")
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "수정 화면", description = "수정 화면")
    @PreAuthorize("authentication.principal.id == #id")
    @GetMapping("/{id}/edit")
    public String getEdit(
            @PathVariable String id,
            Model model
    ) {
        User user = userService.findById(id);
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
            @ModelAttribute("command") UpdateUserCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        userService.update(id, command);
        redirectAttributes.addFlashAttribute("message", "수정 성공");
        return "redirect:/users/" + id + "/edit";
    }

    @Operation(summary = "삭제", description = "삭제")
    @PreAuthorize("authentication.principal.id == #id")
    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable String id,
            RedirectAttributes redirectAttributes
    ) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("message", "삭제 성공");
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
        return "users/edit-password";
    }

    @Operation(summary = "패스워드 수정", description = "패스워드 수정")
    @PreAuthorize("authentication.principal.id == #id")
    @PutMapping("/{id}/password")
    public String updatePassword(
            @PathVariable String id,
            @ModelAttribute("command") UpdateUserPasswordCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "users/edit-password";
        }
        userService.updatePassword(id, command.originPassword(), command.newPassword());
        redirectAttributes.addFlashAttribute("message", "수정 성공");
        return "redirect:/users/" + id + "/edit-password";
    }

}
