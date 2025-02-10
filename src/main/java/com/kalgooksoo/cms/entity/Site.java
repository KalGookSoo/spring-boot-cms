package com.kalgooksoo.cms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kalgooksoo.core.hierarchy.Hierarchical;
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
@EqualsAndHashCode(callSuper = true, exclude = {"parent", "children", "categories", "attachments"})
@ToString(callSuper = true, exclude = {"parent", "children", "categories", "attachments"})

@Entity
@Table(name = "tb_site")
@DynamicInsert
@DynamicUpdate
@Comment("사이트")
public class Site extends BaseEntity implements Hierarchical<Site> {

    @Comment("이름")
    private String name;

    @Comment("설명")
    private String description;

    @Enumerated(EnumType.STRING)
    @Comment("타입")
    private SiteType type;

    @Enumerated(EnumType.STRING)
    @Comment("공개여부")
    private Visibility visibility;

    @Comment("소개")
    private String introduction;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Comment("부모 식별자")
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @JsonBackReference
    private Site parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    private List<Site> children = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "site", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Category> categories = new ArrayList<>();

    @JsonManagedReference
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_site_attachment",
            joinColumns = @JoinColumn(name = "site_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private Set<Attachment> attachments = new LinkedHashSet<>();

    @Override
    public void addChild(Site child) {
        children.add(child);
        parent = this;
    }

}
