package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.HierarchicalFactory;
import com.kalgooksoo.cms.board.command.CreateMenuCommand;
import com.kalgooksoo.cms.board.command.UpdateMenuCommand;
import com.kalgooksoo.cms.board.entity.Menu;
import com.kalgooksoo.cms.board.service.MenuService;
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

@Tag(name = "MenuApiController", description = "메뉴 API 컨트롤러")
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuApiController {

    private final MenuService menuService;

    private final CmsMessageSource messageSource;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Collection<Menu>> get() {
        List<Menu> menus = menuService.findAll();
        return ResponseEntity.ok(HierarchicalFactory.build(menus));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> post(@RequestBody @Valid CreateMenuCommand command) {
        menuService.create(command);
        String message = messageSource.getMessage("command.success.create");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> put(@PathVariable String id, @RequestBody @Valid UpdateMenuCommand command) {
        menuService.update(id, command);
        String message = messageSource.getMessage("command.success.update");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        menuService.delete(id);
        String message = messageSource.getMessage("command.success.delete");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
    }

}
