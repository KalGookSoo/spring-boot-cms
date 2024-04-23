package com.kalgooksoo.cms.user.controller;

import com.kalgooksoo.cms.user.entity.User;
import com.kalgooksoo.cms.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Tag(name = "AdminController", description = "계정 컨트롤러(최고관리자)")
@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @Operation(summary = "계정 목록 화면", description = "계정 목록 화면")
    @GetMapping
    public String getAll(
            @PageableDefault Pageable pageable,
            Model model
    ) {
        Page<User> page = userService.findAll(pageable);
        model.addAttribute("page", page);
        return "users/list";
    }

    @Operation(summary = "계정 목록 저장", description = "계정 목록 저장")
    @PostMapping
    public String saveAll(
            RedirectAttributes redirectAttributes
    ) {
        // TODO Implements
        redirectAttributes.addFlashAttribute("message", "저장 성공");
        return "redirect:/admins";
    }

}
