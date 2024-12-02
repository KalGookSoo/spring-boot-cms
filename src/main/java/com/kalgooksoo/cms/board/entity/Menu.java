package com.kalgooksoo.cms.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kalgooksoo.cms.board.Hierarchical;
import com.kalgooksoo.cms.board.command.CreateMenuCommand;
import com.kalgooksoo.cms.board.command.UpdateMenuCommand;
import com.kalgooksoo.cms.user.entity.Authority;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"parent", "children"})

@Entity
@Table(name = "tb_menu")
@DynamicInsert
@DynamicUpdate
@Comment("메뉴")
public class Menu extends BaseEntity implements Hierarchical<Menu> {

    @Comment("이름")
    private String name;

    @Comment("URI")
    private String uri;

    @Comment("순번")
    private Integer sequence;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Comment("부모 식별자")
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @JsonBackReference
    private Menu parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
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
        Menu parent = new Menu();
        parent.setId(command.parentId());
        menu.parent = parent;
        menu.name = command.name();
        menu.uri = command.uri();
        menu.sequence = command.sequence();
        return menu;
    }

    public void update(UpdateMenuCommand command) {
        Menu parent = new Menu();
        parent.setId(command.parentId());
        this.parent = parent;
        this.name = command.name();
        this.uri = command.uri();
        this.sequence = command.sequence();
    }

    public void addChild(Menu child) {
        children.add(child);
        parent = this;
    }

}
