package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Tag(name = "CategoryController", description = "카테고리 컨트롤러")
@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private final MessageSource messageSource;

    @SuppressWarnings("SameParameterValue")
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String getCategories(Model model) {
        // Query
        List<Category> categories = categoryService.findAll();

        // Model
        model.addAttribute("categories", categories);

        // View
        return "categories/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String getNew(
            @ModelAttribute("command") CreateCategoryCommand command,
            Model model
    ) {
        // Query
        List<Category> categories = categoryService.findAll();

        // Model
        model.addAttribute("categories", categories);

        // View
        return "categories/new";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String create(
            @ModelAttribute("command") @Valid CreateCategoryCommand command,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Validation
        if (bindingResult.hasErrors()) {
            return getNew(command, model);
        }

        // Command
        Category category = categoryService.create(command);

        // Model
        redirectAttributes.addFlashAttribute("message", getMessage("command.success.create", null));

        // View
        return "redirect:/categories/" + category.getId() + "/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String getEdit(
            @PathVariable String id,
            Model model
    ) {
        // Query
        Category category = categoryService.find(id);
        UpdateCategoryCommand command = new UpdateCategoryCommand(category.getName(), category.getType());

        // Model
        model.addAttribute("category", category);
        model.addAttribute("command", command);

        // View
        return "categories/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public String getEdit(
            @PathVariable String id,
            @ModelAttribute("command") @Valid UpdateCategoryCommand command,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Validation
        if (bindingResult.hasErrors()) {
            return getEdit(id, model);
        }

        // Command
        categoryService.update(id, command);

        // Model
        redirectAttributes.addFlashAttribute("message", getMessage("command.success.update", null));

        // View
        return "redirect:/categories/" + id + "/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable String id,
            RedirectAttributes redirectAttributes
    ) {
        // Command
        categoryService.delete(id);

        // Model
        redirectAttributes.addFlashAttribute("message", getMessage("command.success.delete", null));

        // View
        return "redirect:/categories";
    }

}
