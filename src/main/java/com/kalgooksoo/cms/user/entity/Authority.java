package com.kalgooksoo.cms.user.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

/**
 * 권한
 */
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = {"id"})
@SuppressWarnings("JpaDataSourceORMInspection")

@Entity
@Table(name = "tb_authority")
@DynamicInsert
@DynamicUpdate
public class Authority implements Serializable {

    /**
     * 권한 식별자
     */
    @Id
    @UuidGenerator
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToMany(mappedBy = "authorities")
    private final Set<User> users = new LinkedHashSet<>();

    /**
     * 이름
     */
    private String name;

    private Authority(String name, User user) {
        this.name = name;
        this.users.add(user);
    }

    public static Authority create(String name, User user) {
        if (!name.startsWith("ROLE_")) {
            throw new IllegalArgumentException("name must start with 'ROLE_'\n name: " + name);
        }
        return new Authority(name, user);
    }

}
