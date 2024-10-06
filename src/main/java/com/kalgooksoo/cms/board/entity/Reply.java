package com.kalgooksoo.cms.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = PROTECTED)

@Entity
@Table(name = "tb_reply")
@Comment("답글")
@DynamicInsert
public class Reply extends BaseEntity {

    @Comment("상위 답글 식별자")
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Reply parent;

    /**
     * 하위 답글
     */
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Reply> children = new ArrayList<>();


    @Comment("게시글 식별자")
    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;

    /**
     * 첨부파일
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_reply_attachment",
            joinColumns = @JoinColumn(name = "reply_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private Set<Attachment> attachments = new LinkedHashSet<>();

    /**
     * 투표
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_reply_vote",
            joinColumns = @JoinColumn(name = "reply_id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id")
    )
    private Set<Vote> votes = new LinkedHashSet<>();

    @Comment("본문")
    @Lob
    private String content;

    public static Reply create(String content) {
        Reply reply = new Reply();
        reply.content = content;
        return reply;
    }

    public void update(String content) {
        this.content = content;
    }

    public void add(Reply reply) {
        children.add(reply);
        reply.parent = this;
    }

    public void remove(Reply reply) {
        children.remove(reply);
        reply.parent = null;
    }

}
