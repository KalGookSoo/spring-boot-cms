package com.kalgooksoo.cms.board.entity;

import lombok.Getter;

/**
 * 카테고리 타입
 */
@Getter
public enum CategoryType {
    PUBLIC("공개"),
    PRIVATE("비공개");

    private final String description;

    CategoryType(String description) {
        this.description = description;
    }
}
