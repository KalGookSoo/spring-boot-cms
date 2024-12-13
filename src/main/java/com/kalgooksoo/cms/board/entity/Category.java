package com.kalgooksoo.cms.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kalgooksoo.cms.board.Hierarchical;
import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(callSuper = true, exclude = {"parent", "children", "articles"})
@ToString(exclude = {"parent", "children", "articles"})

@Entity
@Table(name = "tb_category")
@DynamicInsert
@DynamicUpdate
@Comment("카테고리")
public class Category extends BaseEntity implements Hierarchical<Category> {

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Article> articles = new ArrayList<>();

    @Comment("이름")
    private String name;

    @Enumerated(EnumType.STRING)
    @Comment("타입")
    private CategoryType type;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Comment("부모 식별자")
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @JsonBackReference
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    private List<Category> children = new ArrayList<>();

    public static Category create(CreateCategoryCommand command, Category parent) {
        Category category = new Category();
        category.parent = parent;
        category.name = command.name();
        category.type = command.type();
        return category;
    }

    public void update(UpdateCategoryCommand command, Category parent) {
        this.parent = parent;
        this.name = command.name();
        this.type = command.type();
    }

    public void addArticle(Article article) {
        articles.add(article);
        article.setCategory(this);
    }

    @Override
    public void addChild(Category child) {
        children.add(child);
        parent = this;
    }

}
