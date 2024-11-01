package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.HierarchicalFactory;
import com.kalgooksoo.cms.board.command.CreateMenuCommand;
import com.kalgooksoo.cms.board.command.UpdateMenuCommand;
import com.kalgooksoo.cms.board.entity.Menu;
import com.kalgooksoo.cms.board.service.MenuService;
import com.kalgooksoo.core.validation.ValidationError;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Tag(name = "MenuApiController", description = "메뉴 API 컨트롤러")
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuApiController {

    private final MenuService menuService;

    private final MessageSource messageSource;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> post(@RequestBody @Valid CreateMenuCommand command) {
        menuService.create(command);
        String message = messageSource.getMessage("command.success.create", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> put(@PathVariable String id, @RequestBody @Valid UpdateMenuCommand command) {
        menuService.update(id, command);
        String message = messageSource.getMessage("command.success.update", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        menuService.delete(id);
        String message = messageSource.getMessage("command.success.delete", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    @GetMapping("/refresh")
    public ResponseEntity<Collection<Menu>> refresh() {
        menuService.refresh();
        List<Menu> categories = menuService.findAll();
        return ResponseEntity.ok(HierarchicalFactory.build(categories));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<ValidationError>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationError> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ValidationError::new)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", errors));
    }

}
