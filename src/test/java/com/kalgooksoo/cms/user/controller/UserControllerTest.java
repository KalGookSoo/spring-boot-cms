package com.kalgooksoo.cms.user.controller;

import com.kalgooksoo.cms.user.entity.Authority;
import com.kalgooksoo.cms.user.entity.User;
import com.kalgooksoo.cms.user.model.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("계정 목록 화면을 요청합니다. 관리자 권한으로 접근 시 계정 목록 화면을 반환합니다.")
    void getListShouldReturnListView() throws Exception {
        // Given
        User user = User.create("admin", "12341234", null, null, null);
        Authority authority = Authority.create("ROLE_ADMIN", user);
        user.addAuthority(authority);
        UserDetails principal = new UserPrincipal(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/list")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("page"))
                .andExpect(MockMvcResultMatchers.view().name("users/list"));
    }

}