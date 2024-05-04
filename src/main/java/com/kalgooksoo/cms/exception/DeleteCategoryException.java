package com.kalgooksoo.cms.exception;

import lombok.Getter;

@Getter
public class DeleteCategoryException extends RuntimeException {

    private final String categoryId;

    public DeleteCategoryException(String message, String categoryId) {
        super(message);
        this.categoryId = categoryId;
    }

}
