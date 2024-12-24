package com.kalgooksoo.cms.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(callSuper = true, exclude = {"articles", "replies"})
@ToString(exclude = {"articles", "replies"})

@Entity
@Table(name = "tb_attachment")
@Comment("첨부파일")
@DynamicInsert
@DynamicUpdate
public class Attachment extends BaseEntity {

    @JsonManagedReference
    @ManyToMany(mappedBy = "attachments")
    private final Set<Article> articles = new LinkedHashSet<>();

    @JsonManagedReference
    @ManyToMany(mappedBy = "attachments")
    private final Set<Reply> replies = new LinkedHashSet<>();

    @Comment("원본이름")
    private String originalName;

    @Comment("이름")
    private String name;

    @Comment("경로명")
    private String pathName;

    @Comment("MIME 타입")
    private String mimeType;

    @Comment("크기")
    private long size;

    public static Attachment create(String pathName, MultipartFile multipartFile) {
        Attachment attachment = new Attachment();
        attachment.originalName = multipartFile.getOriginalFilename();
        attachment.name = generateName(attachment.originalName);
        attachment.pathName = pathName;
        attachment.mimeType = multipartFile.getContentType();
        attachment.size = multipartFile.getSize();
        return attachment;
    }

    private static String generateName(String originName) {
        return String.format("%s_%s", UUID.randomUUID(), originName);
    }

    public String getAbsolutePath() {
        return pathName + "/" + name;
    }

}
