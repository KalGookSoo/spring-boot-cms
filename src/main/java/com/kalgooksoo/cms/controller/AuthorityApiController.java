package com.kalgooksoo.cms.controller;

import com.kalgooksoo.cms.command.AuthoritySaveCommand;
import com.kalgooksoo.cms.entity.Authority;
import com.kalgooksoo.cms.message.CmsMessageSource;
import com.kalgooksoo.cms.service.AuthorityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AuthorityApiController", description = "권한 API 컨트롤러")
@RestController
@RequestMapping("/api/authorities")
@RequiredArgsConstructor
public class AuthorityApiController {

    private final CmsMessageSource messageSource;

    private final AuthorityService authorityService;

    @GetMapping
    public ResponseEntity<List<Authority>> get() {
        List<Authority> authorities = authorityService.findAll();
        return ResponseEntity.ok(authorities);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<String> post(@RequestBody List<@Valid AuthoritySaveCommand> commands) {
        authorityService.saveAll(commands);
        String message = messageSource.getMessage("command.success.save");
        return ResponseEntity.ok(message);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody List<String> ids) {
        authorityService.deleteAll(ids);
        String message = messageSource.getMessage("command.success.delete");
        return ResponseEntity.ok(message);
    }

}
