package com.kalgooksoo.cms.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = {"id"})
@SuppressWarnings("JpaDataSourceORMInspection")

@Entity
@Table(name = "tb_user")
@DynamicInsert
@DynamicUpdate
@Comment("계정")
public class User implements Serializable {

    @Id
    @UuidGenerator
    @Column(length = 36, nullable = false, updatable = false)
    @Comment("식별자")
    private String id;

    @Column(unique = true, nullable = false)
    @Comment("계정명")
    private String username;

    @JsonIgnore
    @Comment("패스워드")
    private String password;

    @Comment("이름")
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "email_id")),
            @AttributeOverride(name = "domain", column = @Column(name = "email_domain"))
    })
    @Comment("이메일")
    private Email email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "first", column = @Column(name = "first_contact_number")),
            @AttributeOverride(name = "middle", column = @Column(name = "middle_contact_number")),
            @AttributeOverride(name = "last", column = @Column(name = "last_contact_number"))
    })
    @Comment("연락처")
    private ContactNumber contactNumber;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_user_authority",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "authorityId")
    )
    private final Set<Authority> authorities = new LinkedHashSet<>();

    @Comment("생성 일시")
    private LocalDateTime createdAt;

    @Comment("수정 일시")
    private LocalDateTime modifiedAt;

    @Comment("만료 일시")
    private LocalDateTime expiredAt;

    @Comment("잠금 일시")
    private LocalDateTime lockedAt;

    @Comment("패스워드 만료 일시")
    private LocalDateTime credentialsExpiredAt;

    public static User create(String username, String password, String name, Email email, ContactNumber contactNumber) {
        User user = new User();
        user.username = username;
        user.password = password;
        user.name = name;
        user.email = email;
        user.contactNumber = contactNumber;
        user.createdAt = LocalDateTime.now();
        user.initializeAccountPolicy();
        return user;
    }

    public void update(String name, Email email, ContactNumber contactNumber) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.modifiedAt = LocalDateTime.now();
    }

    public void changePassword(String password) {
        Assert.notNull(password, "패스워드는 NULL이 될 수 없습니다.");
        this.password = password;
        this.credentialsExpiredAt = LocalDate.now().atTime(LocalTime.MIDNIGHT).plusDays(180L);
        this.modifiedAt = LocalDateTime.now();
    }

    /**
     * 만료 일시는 금일(00:00)로부터 2년 후 까지로 설정합니다.
     * 패스워드 만료 일시는 생성일(00:00)로부터 180일 후 까지로 설정합니다.
     */
    private void initializeAccountPolicy() {
        expiredAt = LocalDate.now().atTime(LocalTime.MIDNIGHT).plusYears(2L);
        credentialsExpiredAt = LocalDate.now().atTime(LocalTime.MIDNIGHT).plusDays(180L);
    }

    /**
     * 계정이 만료되지 않았는지 여부를 반환합니다.
     * @return 계정이 만료되지 않았는지 여부
     */
    public boolean isAccountNonExpired() {
        return expiredAt == null || expiredAt.isAfter(LocalDateTime.now());
    }

    /**
     * 계정이 잠겨있지 않은지 여부를 반환합니다.
     * @return 계정이 잠겨있지 않은지 여부
     */
    public boolean isAccountNonLocked() {
        return lockedAt == null || lockedAt.isBefore(LocalDateTime.now());
    }

    /**
     * 계정의 패스워드가 만료되지 않았는지 여부를 반환합니다.
     * @return 계정의 패스워드가 만료되지 않았는지 여부
     */
    public boolean isCredentialsNonExpired() {
        return credentialsExpiredAt == null || credentialsExpiredAt.isAfter(LocalDateTime.now());
    }

    public void addAuthority(Authority authority) {
        authorities.add(authority);
        authority.getUsers().add(this);
    }

    public void removeAuthority(Authority authority) {
        authorities.remove(authority);
        authority.getUsers().remove(this);
    }

    public void removeAuthorities() {
        authorities.clear();
    }

    public Email getEmail() {
        return Optional.ofNullable(email).orElseGet(Email::new);
    }

    public ContactNumber getContactNumber() {
        return Optional.ofNullable(contactNumber).orElseGet(ContactNumber::new);
    }

}
