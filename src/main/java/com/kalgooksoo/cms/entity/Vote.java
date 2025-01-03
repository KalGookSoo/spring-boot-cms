package com.kalgooksoo.cms.entity;

import jakarta.persistence.*;
import lombok.*;
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
@EqualsAndHashCode(callSuper = true, exclude = {"articles", "replies"})
@ToString(callSuper = true, exclude = {"articles", "replies"})

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
