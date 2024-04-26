package com.kalgooksoo.cms.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

/**
 * 댓글
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("JpaDataSourceORMInspection")

@Entity
@Table(name = "tb_comment")
@DynamicInsert
public class Comment extends BaseEntity {

    /**
     * 상위 댓글
     */
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Comment parent;

    /**
     * 하위 댓글
     */
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Comment> children = new ArrayList<>();

    /**
     * 게시글
     */
    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;

    /**
     * 첨부파일
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_comment_attachment",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private Set<Attachment> attachments = new LinkedHashSet<>();

    /**
     * 투표
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_comment_vote",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id")
    )
    private Set<Vote> votes = new LinkedHashSet<>();

    /**
     * 본문
     */
    @Lob
    private String content;
    public static Comment create(String content, String author) {
        Assert.notNull(author, "작성자는 필수입니다.");
        Comment comment = new Comment();
        comment.content = content;
        return comment;
    }

    public void update(String content) {
        this.content = content;
    }

    public void add(Comment comment) {
        children.add(comment);
        comment.parent = this;
    }

    public void remove(Comment comment) {
        children.remove(comment);
        comment.parent = null;
    }

}
