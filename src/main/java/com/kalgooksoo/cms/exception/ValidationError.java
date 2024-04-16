package com.kalgooksoo.cms.exception;

public record ValidationError(String code, String message, String field, Object rejectedValue) {}