package com.kalgooksoo.cms.controller;

import com.kalgooksoo.core.hierarchy.HierarchicalFactory;
import com.kalgooksoo.cms.entity.Category;
import com.kalgooksoo.cms.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "CategoryController", description = "카테고리 컨트롤러")
@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/list")
    public String getCategories(Model model) {
        // Query
        List<Category> categories = categoryService.findAll();

        // Model
        model.addAttribute("refreshTime", LocalDateTime.now());
        model.addAttribute("categories", HierarchicalFactory.build(categories));

        // View
        return "categories/list";
    }

}
