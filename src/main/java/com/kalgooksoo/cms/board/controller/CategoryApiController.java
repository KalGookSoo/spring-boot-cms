package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.HierarchicalFactory;
import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.service.CategoryService;
import com.kalgooksoo.cms.message.CmsMessageSource;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Tag(name = "CategoryApiController", description = "카테고리 API 컨트롤러")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    private final CmsMessageSource messageSource;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Collection<Category>> get() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(HierarchicalFactory.build(categories));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> post(@RequestBody @Valid CreateCategoryCommand command) {
        categoryService.create(command);
        String message = messageSource.getMessage("command.success.create");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> put(@PathVariable String id, @RequestBody @Valid UpdateCategoryCommand command) {
        categoryService.update(id, command);
        String message = messageSource.getMessage("command.success.update");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        categoryService.delete(id);
        String message = messageSource.getMessage("command.success.delete");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
    }

}
