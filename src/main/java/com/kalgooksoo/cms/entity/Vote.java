package com.kalgooksoo.cms.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.LinkedHashSet;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

/**
 * 투표
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = PROTECTED)

@Entity
@Table(name = "tb_vote")
@DynamicInsert
@DynamicUpdate
public class Vote extends BaseEntity {

    /**
     * 게시글
     */
    @ManyToMany(mappedBy = "votes")
    private final Set<Article> articles = new LinkedHashSet<>();

    /**
     * 답글
     */
    @ManyToMany(mappedBy = "votes")
    private final Set<Reply> replies = new LinkedHashSet<>();

    /**
     * 타입
     */
    @Enumerated(EnumType.STRING)
    private VoteType type;

}
