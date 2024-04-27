package com.kalgooksoo.cms.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

/**
 * 카테고리
 */
@Getter
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("JpaDataSourceORMInspection")

@Entity
@Table(name = "tb_category")
@DynamicInsert
@DynamicUpdate
public class Category extends BaseEntity {

    /**
     * 상위 카테고리
     */
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Category parent;

    /**
     * 하위 카테고리
     */
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Category> children = new ArrayList<>();

    /**
     * 게시글
     */
    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Article> articles = new ArrayList<>();

    /**
     * 이름
     */
    private String name;

    /**
     * 타입
     */
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    public static Category create(String name, CategoryType type) {
        Category category = new Category();
        category.name = name;
        category.type = type;
        return category;
    }

    public void update(String name, CategoryType type) {
        this.name = name;
        this.type = type;
    }

    public void addCategory(Category category) {
        children.add(category);
        category.parent = this;
    }

    public void removeCategory(Category category) {
        children.remove(category);
        category.parent = null;
    }

    public void addArticle(Article article) {
        articles.add(article);
        article.setCategory(this);
    }

    public void removeArticle(Article article) {
        articles.remove(article);
        article.setCategory(null);
    }

}
