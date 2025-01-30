package com.kalgooksoo.cms;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 메인 컨트롤러
 */
@Controller
public class MainController {

    /**
     * 메인 화면을 반환합니다.
     *
     * @return 메인 화면
     */
    @GetMapping("/")
    public String index() {
        return "main";
    }

    /**
     * 세션 식별자를 반환합니다.
     *
     * @param request 요청
     * @return 세션 식별자
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> getSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", session.getId());

        Map<String, Object> attributes = new HashMap<>();
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            attributes.put(attributeName, session.getAttribute(attributeName));
        }
        responseBody.put("attributes", attributes);

        return ResponseEntity.ok(responseBody);
    }

}