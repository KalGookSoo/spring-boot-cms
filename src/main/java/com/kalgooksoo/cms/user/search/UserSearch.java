package com.kalgooksoo.cms.user.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kalgooksoo.cms.user.entity.ContactNumber;
import com.kalgooksoo.cms.user.entity.Email;
import com.kalgooksoo.core.page.PageVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 계정 검색 조건
 */
@Getter
@Setter
@Schema(description = "계정 검색 조건")
public class UserSearch extends PageVO {

    private String username;

    private String name;

    private Email email;

    private ContactNumber contactNumber;

    @JsonIgnore
    public boolean isEmptyUsername() {
        return username == null || username.isEmpty();
    }

    @JsonIgnore
    public boolean isEmptyName() {
        return name == null || name.isEmpty();
    }

    @JsonIgnore
    public boolean isEmptyEmail() {
        return email == null || email.getValue().isEmpty();
    }

    @JsonIgnore
    public boolean isEmptyContactNumber() {
        return contactNumber == null || contactNumber.getValue().isEmpty();
    }

}