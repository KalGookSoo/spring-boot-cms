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
public class Email implements Serializable {

    private String id;

    private String domain;

    public String getValue() {
        return id + "@" + domain;
    }

    public Email(String id, String domain) {
        Assert.notNull(id, "Email id must not be null");
        Assert.notNull(domain, "Email domain must not be null");
        this.id = id;
        this.domain = domain;
    }

}
