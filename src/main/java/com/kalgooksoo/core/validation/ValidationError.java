package com.kalgooksoo.core.validation;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;

import java.util.Objects;

public final class ValidationError {
    private final String code;
    private final String message;
    private final String field;
    private final Object rejectedValue;

    public ValidationError(FieldError error) {
        this.code = error.getCode();
        this.message = error.getDefaultMessage();
        this.field = error.getField();
        this.rejectedValue = error.getRejectedValue();
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public String field() {
        return field;
    }

    public Object rejectedValue() {
        return rejectedValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ValidationError) obj;
        return Objects.equals(this.code, that.code) &&
                Objects.equals(this.message, that.message) &&
                Objects.equals(this.field, that.field) &&
                Objects.equals(this.rejectedValue, that.rejectedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, field, rejectedValue);
    }

    @Override
    public String toString() {
        return "ValidationError[" +
                "code=" + code + ", " +
                "message=" + message + ", " +
                "field=" + field + ", " +
                "rejectedValue=" + rejectedValue + ']';
    }
}