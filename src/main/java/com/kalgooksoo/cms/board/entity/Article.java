package com.kalgooksoo.cms.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("JpaDataSourceORMInspection")

@Entity
@Table(name = "tb_article")
@Comment("게시글")
@DynamicInsert
@DynamicUpdate
public class Article extends BaseEntity {

    @Comment("카테고리 식별자")
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<View> views = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_article_attachment",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private Set<Attachment> attachments = new LinkedHashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_article_vote",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id")
    )
    private Set<Vote> votes = new LinkedHashSet<>();

    @Comment("제목")
    private String title;

    @Lob
    @Comment("본문")
    private String content;

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

    public void addReply(Reply reply) {
        replies.add(reply);
        reply.setArticle(this);
    }

    public void removeReply(Reply reply) {
        replies.remove(reply);
        reply.setArticle(null);
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
