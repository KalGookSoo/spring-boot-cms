package com.kalgooksoo.cms.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.LinkedHashSet;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

/**
 * 첨부파일
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("JpaDataSourceORMInspection")

@Entity
@Table(name = "tb_attachment")
@DynamicInsert
@DynamicUpdate
public class Attachment extends BaseEntity {

    @ManyToMany(mappedBy = "attachments")
    private final Set<Article> articles = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "attachments")
    private final Set<Comment> comments = new LinkedHashSet<>();

    /**
     * 이름
     */
    private String name;

    /**
     * 경로명
     */
    private String pathName;

    /**
     * MIME 타입
     */
    private String mimeType;

    /**
     * 크기
     */
    private long size;

    public static Attachment create(String name, String pathName, String mimeType, long size) {
        Attachment attachment = new Attachment();
        attachment.name = name;
        attachment.pathName = pathName;
        attachment.mimeType = mimeType;
        attachment.size = size;
        return attachment;
    }

}
