package com.kalgooksoo.cms.entity;

import lombok.Getter;

/**
 * 공개여부
 */
@Getter
public enum Visibility {
    PUBLIC("공개"),
    PRIVATE("비공개");

    private final String description;

    Visibility(String description) {
        this.description = description;
    }
}
