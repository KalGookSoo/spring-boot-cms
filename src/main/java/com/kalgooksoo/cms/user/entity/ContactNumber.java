package com.kalgooksoo.cms.user.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.util.Assert;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

/**
 * 연락처
 */
@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = PROTECTED)
public class ContactNumber implements Serializable {

    private String first;

    private String middle;

    private String last;

    /**
     * 연락처 표현식을 반환합니다.
     * @return 연락처 표현식
     */
    public String getValue() {
        return first + "-" + middle + "-" + last;
    }

    /**
     * 연락처 생성자
     * @param first  연락처 첫번째 자리
     * @param middle 연락처 중간 자리
     * @param last   연락처 마지막 자리
     */
    public ContactNumber(String first, String middle, String last) {
        Assert.notNull(first, "first must not be null");
        Assert.notNull(middle, "middle must not be null");
        Assert.notNull(last, "last must not be null");
        this.first = first;
        this.middle = middle;
        this.last = last;
    }
}
