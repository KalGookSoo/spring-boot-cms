package com.kalgooksoo.cms.board.entity;

import com.kalgooksoo.cms.board.Hierarchical;
import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
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
public class Category extends BaseEntity implements Hierarchical<Category, String> {

    @Comment("부모 식별자")
    @Column(name = "parent_id")
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private String parentId;

    @Transient
    private List<Category> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Article> articles = new ArrayList<>();

    @Comment("이름")
    private String name;

    @Enumerated(EnumType.STRING)
    @Comment("타입")
    private CategoryType type;

    public static Category create(CreateCategoryCommand command) {
        Category category = new Category();
        category.parentId = command.parentId();
        category.name = command.name();
        category.type = command.type();
        return category;
    }

    public void update(UpdateCategoryCommand command) {
        this.parentId = command.parentId();
        this.name = command.name();
        this.type = command.type();
    }

    public void addArticle(Article article) {
        articles.add(article);
        article.setCategory(this);
    }

    @Override
    public void setChildren(List<Category> children) {
        this.children = children;
    }

}
