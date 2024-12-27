package com.kalgooksoo.cms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.kalgooksoo.cms.entity.Authority;
import com.kalgooksoo.cms.message.CmsMessageSource;
import com.kalgooksoo.cms.service.AuthorityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<String> post(@RequestBody JsonNode jsonNode) {
        System.out.println(jsonNode);
//        authorityService.saveAll(null);
        String message = messageSource.getMessage("command.success.save");
        return ResponseEntity.ok(message);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody JsonNode jsonNode) {
        System.out.println(jsonNode);
//        authorityService.deleteAll(null);
        String message = messageSource.getMessage("command.success.delete");
        return ResponseEntity.ok(message);
    }

}
