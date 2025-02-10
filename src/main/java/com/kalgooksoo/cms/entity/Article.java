package com.kalgooksoo.cms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kalgooksoo.cms.command.CreateArticleCommand;
import com.kalgooksoo.cms.command.UpdateArticleCommand;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(callSuper = true, exclude = {"category", "replies", "views", "attachments", "votes"})
@ToString(callSuper = true, exclude = {"category", "replies", "views", "attachments", "votes"})

@Entity
@Table(name = "tb_article")
@Comment("게시글")
@DynamicInsert
@DynamicUpdate
public class Article extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Comment("공개여부")
    private Visibility visibility;

    @Comment("제목")
    private String title;

    @Lob
    @Comment("본문")
    private String content;

        @JsonBackReference
    @Comment("카테고리 식별자")
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @JsonManagedReference
    @OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Reply> replies = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<View> views = new ArrayList<>();

    @JsonManagedReference
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_article_attachment",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private Set<Attachment> attachments = new LinkedHashSet<>();

    @JsonManagedReference
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_article_vote",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id")
    )
    private Set<Vote> votes = new LinkedHashSet<>();

    public static Article create(CreateArticleCommand command) {
        Article article = new Article();
        article.title = command.getTitle();
        article.content = Jsoup.clean(command.getContent(), Safelist.relaxed());
        return article;
    }

    public void update(UpdateArticleCommand command) {
        this.title = command.getTitle();
        this.content = Jsoup.clean(command.getContent(), Safelist.relaxed());
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

    public void addVote(Vote vote) {
        votes.add(vote);
        vote.getArticles().add(this);
    }

}
