package com.kalgooksoo.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionApiController {

    @GetMapping("/refresh")
    ResponseEntity<?> refresh() {
        return ResponseEntity.ok().body(null);
    }

}