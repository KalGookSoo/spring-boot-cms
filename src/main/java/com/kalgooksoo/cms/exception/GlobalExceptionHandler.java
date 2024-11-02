package com.kalgooksoo.cms.exception;

import com.kalgooksoo.core.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 전역 예외 핸들러
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 요청이 잘못된 경우
     * @param e IllegalArgumentException
     * @return 400 에러 화면
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error(e.getMessage());
        return "error/400";
    }

    /**
     * 해당 자원을 찾을 수 없는 경우
     * @param e NoSuchElementException
     * @return 404 에러 화면
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e) {
        logger.error(e.getMessage());
        return "error/404";
    }

    /**
     * 자원에 대한 접근 권한이 없는 경우
     * @param e AccessDeniedException
     * @return 계정 인증 화면
     */
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException e) {
        logger.error(e.getMessage());
        return "redirect:/sign-in";
    }

    /**
     * 인증되지 않은 사용자가 접근한 경우
     * @param e AuthenticationException
     * @return 계정 인증 화면
     */
    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException e) {
        logger.error(e.getMessage());
        return "redirect:/sign-in";
    }

    /**
     * 핸들링할 수 없는 오류가 발생한 경우
     * @param e RuntimeException
     * @return 500 에러 화면
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e) {
        logger.error(e.getMessage());
        return "error/500";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<ValidationError>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationError> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ValidationError::new)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", errors));
    }

}
