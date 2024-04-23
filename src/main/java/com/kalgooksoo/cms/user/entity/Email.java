package com.kalgooksoo.cms.user.entity;


import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Email implements Serializable {

    private String id;

    private String domain;

    public String getValue() {
        return id + "@" + domain;
    }

}
