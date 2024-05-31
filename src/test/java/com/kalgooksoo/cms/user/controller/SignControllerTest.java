package com.kalgooksoo.cms.user.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

/**
 * 계정 인증 컨트롤러 테스트
 * @see SignController
 */
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("계정 인증 화면 요청합니다. 인증되지 않았다면 계정 인증 화면을 반환합니다.")
    @WithAnonymousUser
    void signInShouldReturnSignInView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sign-in"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("sign_in"));
    }

    @Test
    @DisplayName("계정 인증 화면 요청합니다. 이미 인증되었다면 메인 화면을 반환합니다.")
    @WithMockUser(roles = "USER")
    void signInShouldReturnMainView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sign-in"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

}