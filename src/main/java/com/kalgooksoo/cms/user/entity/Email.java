package com.kalgooksoo.cms.user.entity;


import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.util.Assert;

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
        Assert.notNull(id, "Email id must not be null");
        Assert.notNull(domain, "Email domain must not be null");
        this.id = id;
        this.domain = domain;
    }

}
