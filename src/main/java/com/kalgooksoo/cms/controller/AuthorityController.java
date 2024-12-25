package com.kalgooksoo.cms.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 권한 컨트롤러
 */
@Tag(name = "AuthorityController", description = "권한 컨트롤러")
@Controller
@RequestMapping("/authorities")
@RequiredArgsConstructor
public class AuthorityController {

    @GetMapping("/list")
    public String getList() {
        return "authorities/list";
    }

}
