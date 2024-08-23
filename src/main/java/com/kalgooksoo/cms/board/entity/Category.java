package com.kalgooksoo.cms.board.entity;

import com.kalgooksoo.cms.board.Hierarchical;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)

@Entity
@Table(name = "tb_category")
@DynamicInsert
@DynamicUpdate
@Comment("카테고리")
public class Category extends BaseEntity implements Hierarchical<Category> {

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @Comment("부모 식별자")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Article> articles = new ArrayList<>();

    @Comment("이름")
    private String name;

    @Enumerated(EnumType.STRING)
    @Comment("타입")
    private CategoryType type;

    public static Category create(Category parent, String name, CategoryType type) {
        Category category = new Category();
        category.parent = parent;
        category.name = name;
        category.type = type;
        return category;
    }

    public void update(Category parent, String name, CategoryType type) {
        this.parent = parent;
        this.name = name;
        this.type = type;
    }

    public void addArticle(Article article) {
        articles.add(article);
        article.setCategory(this);
    }

    public void removeArticle(Article article) {
        articles.remove(article);
        article.setCategory(null);
    }

    @Override
    public void setChildren(List<Category> children) {
        this.children = children;
    }

    @Override
    public void moveTo(Category parent) {
        this.parent = parent;
    }

}
