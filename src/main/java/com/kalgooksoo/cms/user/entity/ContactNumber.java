package com.kalgooksoo.cms.user.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.util.Assert;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = PROTECTED)
public class ContactNumber implements Serializable {

    private String first;

    private String middle;

    private String last;

    public String getValue() {
        return first + "-" + middle + "-" + last;
    }

    public ContactNumber(String first, String middle, String last) {
        Assert.notNull(first, "first must not be null");
        Assert.notNull(middle, "middle must not be null");
        Assert.notNull(last, "last must not be null");
        this.first = first;
        this.middle = middle;
        this.last = last;
    }
}
