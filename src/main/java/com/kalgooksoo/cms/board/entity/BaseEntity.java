package com.kalgooksoo.cms.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "id")
@Getter
@Setter(AccessLevel.PROTECTED)
public class BaseEntity {

    @Id
    @UuidGenerator
    @Column(length = 36, nullable = false, updatable = false)
    @Comment("식별자")
    private String id;

    @CreatedBy
    @Comment("생성자")
    private String createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("생성일시")
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Comment("수정자")
    private String lastModifiedBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("수정일시")
    private LocalDateTime lastModifiedDate;

}
