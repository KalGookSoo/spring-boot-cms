package com.kalgooksoo.cms.board.entity;

import com.kalgooksoo.cms.board.Hierarchical;
import com.kalgooksoo.cms.board.command.CreateMenuCommand;
import com.kalgooksoo.cms.board.command.UpdateMenuCommand;
import com.kalgooksoo.cms.user.entity.Authority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)

@Entity
@Table(name = "tb_menu")
@DynamicInsert
@DynamicUpdate
@Comment("메뉴")
public class Menu extends BaseEntity implements Hierarchical<Menu, String> {

    @Comment("이름")
    private String name;

    @Comment("URI")
    private String uri;

    @Comment("순서")
    private Integer sequence;

    @Comment("부모 식별자")
    @Column(name = "parent_id")
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private String parentId;

    @Transient
    private List<Menu> children = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_menu_authority",
            joinColumns = @JoinColumn(name = "menuId"),
            inverseJoinColumns = @JoinColumn(name = "authorityId")
    )
    private final Set<Authority> authorities = new LinkedHashSet<>();

    public static Menu create(CreateMenuCommand command) {
        Menu menu = new Menu();
        menu.parentId = command.parentId();
        menu.name = command.name();
        menu.uri = command.uri();
        return menu;
    }

    public void update(UpdateMenuCommand command) {
        this.parentId = command.parentId();
        this.name = command.name();
        this.uri = command.uri();
    }

    @Override
    public void setChildren(List<Menu> children) {
        this.children = children;
    }

}
