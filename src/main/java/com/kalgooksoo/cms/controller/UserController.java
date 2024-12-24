package com.kalgooksoo.cms.controller;

import com.kalgooksoo.cms.message.CmsMessageSource;
import com.kalgooksoo.cms.command.UpdateUserCommand;
import com.kalgooksoo.cms.command.UpdateUserPasswordCommand;
import com.kalgooksoo.cms.entity.Authority;
import com.kalgooksoo.cms.entity.User;
import com.kalgooksoo.cms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

/**
 * 계정 컨트롤러
 */
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final CmsMessageSource messageSource;

    /**
     * 계정 목록 화면을 반환합니다.
     * @param pageable 페이지네이션 요청 정보
     * @param model    모델
     * @return 계정 목록 화면
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public String getList(
            @PageableDefault Pageable pageable,
            Model model
    ) {
        // Query
        Page<User> page = userService.findAll(pageable);

        // Model
        model.addAttribute("page", page);

        // View
        return "users/list";
    }

    /**
     * 계정 수정 화면을 반환합니다.
     * @param id    계정 식별자
     * @param model 모델
     * @return 계정 수정 화면
     */
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String getEdit(
            @PathVariable String id,
            Model model
    ) {
        // Query
        User user = userService.findById(id);
        Set<Authority> authorities = user.getAuthorities();
        UpdateUserCommand command = new UpdateUserCommand(
                user.getName(),
                user.getEmail().getId(),
                user.getEmail().getDomain(),
                user.getContactNumber().getFirst(),
                user.getContactNumber().getMiddle(),
                user.getContactNumber().getLast()
        );

        // Model
        model.addAttribute("user", user);
        model.addAttribute("authorities", authorities);
        model.addAttribute("command", command);

        // View
        return "users/edit";
    }

    /**
     * 계정 수정 처리합니다.
     * @param id                 계정 식별자
     * @param command            계정 수정 커맨드
     * @param bindingResult      검증 결과
     * @param model              모델
     * @param redirectAttributes 리다이렉트 속성
     * @return 계정 수정 화면
     */
    @PreAuthorize("authentication.principal.id == #id")
    @PutMapping("/{id}")
    public String update(
            @PathVariable String id,
            @ModelAttribute("command") @Valid UpdateUserCommand command,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Validation
        if (bindingResult.hasErrors()) {
            return getEdit(id, model);
        }

        // Command
        userService.update(id, command);

        // Model
        String message = messageSource.getMessage("command.success.update");
        redirectAttributes.addFlashAttribute("message", message);

        // View
        return "redirect:/users/" + id + "/edit";
    }

    /**
     * 계정 삭제 처리합니다.
     * @param id                 계정 식별자
     * @param request            요청
     * @param response           응답
     * @param redirectAttributes 리다이렉트 속성
     * @return 메인 화면
     */
    @PreAuthorize("authentication.principal.id == #id")
    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable String id,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes
    ) {
        // Command
        userService.delete(id);

        // Clear SecurityContext and invalidate HttpSession
        new SecurityContextLogoutHandler().logout(request, response, null);

        // Delete remember-me cookie
        new CookieClearingLogoutHandler("remember-me").logout(request, response, null);

        // Model
        String message = messageSource.getMessage("command.success.delete");
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/";
    }

    /**
     * 계정 패스워드 수정 화면을 반환합니다.
     * @param id    계정 식별자
     * @param model 모델
     * @return 계정 패스워드 수정 화면
     */
    @PreAuthorize("authentication.principal.id == #id")
    @GetMapping("/{id}/edit-password")
    public String getEditPassword(
            @PathVariable String id,
            Model model
    ) {
        // Query
        UpdateUserPasswordCommand command = new UpdateUserPasswordCommand(null, null);

        // Model
        model.addAttribute("command", command);

        // View
        return "users/edit_password";
    }

    /**
     * 계정 패스워드 수정 처리합니다.
     * @param id                 계정 식별자
     * @param command            계정 패스워드 수정 커맨드
     * @param bindingResult      검증 결과
     * @param redirectAttributes 리다이렉트 속성
     * @return 계정 패스워드 수정 화면
     */
    @PreAuthorize("authentication.principal.id == #id")
    @PutMapping("/{id}/password")
    public String updatePassword(
            @PathVariable String id,
            @ModelAttribute("command") @Valid UpdateUserPasswordCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        // Validation
        if (bindingResult.hasErrors()) {
            return "users/edit_password";
        }

        // Command
        try {// TODO 에러 핸들링 중앙화할 것
            userService.updatePassword(id, command.originPassword(), command.newPassword());
        } catch (IllegalArgumentException e) {
            //noinspection DataFlowIssue
            bindingResult.rejectValue("originPassword", null, e.getMessage());
            return "users/edit_password";
        }

        // Model
        String message = messageSource.getMessage("command.success.update");
        redirectAttributes.addFlashAttribute("message", message);

        // View
        return "redirect:/users/" + id + "/edit-password";
    }

}
