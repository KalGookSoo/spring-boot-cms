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
public class ContactNumber implements Serializable {

    private String first;

    private String middle;

    private String last;

    public String getValue() {
        return first + "-" + middle + "-" + last;
    }

}
