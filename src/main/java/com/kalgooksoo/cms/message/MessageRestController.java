package com.kalgooksoo.cms.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageRestController {

    private final CmsMessageSource messageSource;

    @GetMapping("/messages")
    public ResponseEntity<Map<String, String>> getAllMessages() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        Map<String, String> messages = resourceBundle.keySet()
                .stream()
                .collect(Collectors.toMap(key -> key, resourceBundle::getString));
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{code}")
    public ResponseEntity<String> getMessageByCode(@PathVariable String code, @RequestParam(required = false) String[] args) {
        String message = messageSource.getMessage(code, args);
        return ResponseEntity.ok(message);
    }

}
