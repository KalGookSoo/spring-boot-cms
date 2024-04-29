package com.kalgooksoo.cms;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.NoSuchElementException;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "main";
    }

    @GetMapping("/400")
    public String error400() {
        throw new IllegalArgumentException();
    }

    @GetMapping("/404")
    public String error404() {
        throw new NoSuchElementException();
    }

    @GetMapping("/500")
    public String error500() {
        throw new RuntimeException();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    @GetMapping("/session")
    public String getSession(HttpServletRequest request) {
        return request.getSession().getId();
    }

}