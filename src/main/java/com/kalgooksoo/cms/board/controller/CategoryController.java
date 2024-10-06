package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.service.CategoryService;
import com.kalgooksoo.cms.exception.DeleteCategoryException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;

@Tag(name = "CategoryController", description = "카테고리 컨트롤러")
@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CategoryService categoryService;

    private final MessageSource messageSource;

    @SuppressWarnings("SameParameterValue")
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    @GetMapping("/refresh")
    public ResponseEntity<Collection<Category>> refresh() {
        categoryService.refresh();
        return ResponseEntity.ok(categoryService.findAllNested());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public String getCategories(Model model) {
        // Query
        Collection<Category> categories = categoryService.findAllNested();

        // Model
        model.addAttribute("categories", categories);

        // View
        return "categories/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String getNew(
            @ModelAttribute("command") CreateCategoryCommand command,
            BindingResult bindingResult,
            Model model
    ) {
        // Query
        Collection<Category> categories = categoryService.findAll();
        command = bindingResult.hasErrors() ? command : new CreateCategoryCommand(null, null, null);

        // Model
        model.addAttribute("categories", categories);
        model.addAttribute("command", command);

        // View
        return "categories/new";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String create(
            @ModelAttribute("command") @Valid CreateCategoryCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        // Validation
        if (bindingResult.hasErrors()) {
            return getNew(command, bindingResult, model);
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
            @ModelAttribute("command") UpdateCategoryCommand command,
            BindingResult bindingResult,
            Model model
    ) {
        // Query
        Collection<Category> categories = categoryService.findAll();

        // 부모 노드를 변경하려면
        Category category = categoryService.find(id);
        removeCategoryAndItsChildren(categories, category);
        String parentId = category.getParent() != null ? category.getParent().getId() : null;
        command = bindingResult.hasErrors() ? command : new UpdateCategoryCommand(parentId, category.getName(), category.getType());

        // Model
        model.addAttribute("categories", categories);
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
            return getEdit(id, command, bindingResult, model);
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
    ) throws DeleteCategoryException {
        // Command
        try {
            categoryService.delete(id);
        } catch (DataIntegrityViolationException e) {
            throw new DeleteCategoryException(e.getMessage(), id);
        }

        // Model
        redirectAttributes.addFlashAttribute("message", getMessage("command.success.delete", null));

        // View
        return "redirect:/categories/list";
    }

    @ExceptionHandler(DeleteCategoryException.class)
    public String handleCategoryConstraintException(
            DeleteCategoryException e,
            RedirectAttributes redirectAttributes
    ) {
        logger.error(e.getMessage());

        // Model
        redirectAttributes.addFlashAttribute("message", getMessage("error.constraint.violation", null));

        // View
        return "redirect:/categories/" + e.getCategoryId() + "/edit";
    }

    void removeCategoryAndItsChildren(Collection<Category> categories, Category category) {
        categories.remove(category);
        for (Category subCategory : category.getChildren()) {
            removeCategoryAndItsChildren(categories, subCategory);
        }
    }

}
