package com.kalgooksoo.cms.controller;

import com.kalgooksoo.cms.entity.Authority;
import com.kalgooksoo.cms.service.AuthorityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "AuthorityApiController", description = "권한 API 컨트롤러")
@RestController
@RequestMapping("/api/authorities")
@RequiredArgsConstructor
public class AuthorityApiController {

    private final AuthorityService authorityService;

    @GetMapping
    public ResponseEntity<List<Authority>> get() {
        List<Authority> authorities = authorityService.findAll();
        return ResponseEntity.ok(authorities);
    }

}
