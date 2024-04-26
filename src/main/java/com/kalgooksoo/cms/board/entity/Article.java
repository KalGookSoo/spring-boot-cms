package com.kalgooksoo.cms.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

/**
 * 게시글
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("JpaDataSourceORMInspection")

@Entity
@Table(name = "tb_article")
@DynamicInsert
@DynamicUpdate
public class Article extends BaseEntity {

    /**
     * 카테고리
     */
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    /**
     * 댓글
     */
    @OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Comment> comments = new ArrayList<>();

    /**
     * 조회
     */
    @OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<View> views = new ArrayList<>();

    /**
     * 첨부파일
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_article_attachment",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private Set<Attachment> attachments = new LinkedHashSet<>();

    /**
     * 투표
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_article_vote",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id")
    )
    private Set<Vote> votes = new LinkedHashSet<>();

    /**
     * 제목
     */
    private String title;

    /**
     * 본문
     */
    @Lob
    private String content;

    /**
     * 작성자
     */
    private String author;

    /**
     * 생성 일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정 일시
     */
    private LocalDateTime modifiedAt;

    public static Article create(String title, String content) {
        Article article = new Article();
        article.title = title;
        article.content = content;
        return article;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setArticle(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setArticle(null);
    }

    public void addView(View view) {
        views.add(view);
        view.setArticle(this);
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
        attachment.getArticles().add(this);
    }

    public void removeAttachment(Attachment attachment) {
        attachments.remove(attachment);
        attachment.getArticles().remove(attachment);
    }

    public void addVote(Vote vote) {
        votes.add(vote);
        vote.getArticles().add(this);
    }

    public void removeVote(Vote vote) {
        votes.remove(vote);
        vote.getArticles().remove(vote);
    }

}
