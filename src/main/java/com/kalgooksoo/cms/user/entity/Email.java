package com.kalgooksoo.cms.user.entity;


import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

/**
 * 이메일
 */
@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = PROTECTED)
public class Email implements Serializable {

    private String id;

    private String domain;

    /**
     * 이메일 표현식을 반환합니다.
     * @return 이메일 표현식
     */
    public String getValue() {
        return id + "@" + domain;
    }

    /**
     * 이메일 생성자
     * @param id     이메일 아이디
     * @param domain 이메일 도메인
     */
    public Email(String id, String domain) {
        this.id = id;
        this.domain = domain;
    }

}
