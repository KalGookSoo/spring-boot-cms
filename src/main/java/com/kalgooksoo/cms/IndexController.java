package com.kalgooksoo.cms;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.NoSuchElementException;

/**
 * 인덱스 컨트롤러
 */
@Controller
public class IndexController {

    /**
     * 메인 화면을 반환합니다.
     * @return 메인 화면
     */
    @GetMapping("/")
    public String index() {
        return "main";
    }

    /**
     * IllegalArgumentException을 던집니다.
     * @throws IllegalArgumentException 잘못된 인자 예외
     */
    @GetMapping("/400")
    public String error400() {
        throw new IllegalArgumentException();
    }

    /**
     * NoSuchElementException을 던집니다.
     * @throws NoSuchElementException 요소를 찾을 수 없는 예외
     */
    @GetMapping("/404")
    public String error404() {
        throw new NoSuchElementException();
    }

    /**
     * RuntimeException을 던집니다.
     * @throws RuntimeException 내부 서버 예외
     */
    @GetMapping("/500")
    public String error500() {
        throw new RuntimeException();
    }

    /**
     * 세션 식별자를 반환합니다.
     * @param request 요청
     * @return 세션 식별자
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    @GetMapping("/session")
    public String getSession(HttpServletRequest request) {
        return request.getSession().getId();
    }

}