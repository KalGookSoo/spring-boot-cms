package com.kalgooksoo.cms.controller;

import com.kalgooksoo.cms.command.CreateUserCommand;
import com.kalgooksoo.cms.command.SignInCommand;
import com.kalgooksoo.cms.controller.SignController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

/**
 * 계정 인증 컨트롤러 테스트
 *
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
    @DisplayName("계정 인증 화면을 요청합니다. 인증되지 않았다면 계정 인증 화면을 반환합니다.")
    @WithAnonymousUser
    void signInShouldReturnSignInView() throws Exception {
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/sign-in"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("sign_in"));
    }

    @Test
    @DisplayName("계정 인증 화면을 요청합니다. 이미 인증 성공 시 302 응답코드를 반환합니다.")
    @WithMockUser(roles = "USER")
    void signInShouldReturnMainView() throws Exception {
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/sign-in"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    @DisplayName("계정 생성 화면을 요청합니다. 계정 생성 화면을 반환 합니다.")
    void signUpShouldReturnSignUpView() throws Exception {
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/sign-up"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("command"))
                .andExpect(MockMvcResultMatchers.view().name("sign_up"));
    }

    @Test
    @DisplayName("계정 생성 처리합니다. 값 검증 실패 시 errors 속성과 함께 계정 생성 화면을 반환합니다.")
    void signUpShouldReturnSignUpViewWithErrors() throws Exception {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "1234", "테스터1", null, null, null, null, null);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", createUserCommand.username())
                        .param("password", createUserCommand.password())
                        .param("name", createUserCommand.name())
                        .param("emailId", createUserCommand.emailId())
                        .param("emailDomain", createUserCommand.emailDomain())
                        .param("firstContactNumber", createUserCommand.firstContactNumber())
                        .param("middleContactNumber", createUserCommand.middleContactNumber())
                        .param("lastContactNumber", createUserCommand.lastContactNumber()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.view().name("sign_up"));
    }

    @Test
    @DisplayName("계정 생성 처리합니다. 생성 성공 시 302 응답코드를 반환합니다.")
    void signUpShouldReturnFound() throws Exception {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", "tester@test.com", "test.com", "010", "1234", "5678");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", createUserCommand.username())
                        .param("password", createUserCommand.password())
                        .param("name", createUserCommand.name())
                        .param("emailId", createUserCommand.emailId())
                        .param("emailDomain", createUserCommand.emailDomain())
                        .param("firstContactNumber", createUserCommand.firstContactNumber())
                        .param("middleContactNumber", createUserCommand.middleContactNumber())
                        .param("lastContactNumber", createUserCommand.lastContactNumber()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/sign-in"));
    }

    @Test
    @DisplayName("계정 인증 처리합니다. 인증 실패 시 계정 인증 화면을 요청합니다.")
    void signInTestWithInvalidValues() throws Exception {
        // Given
        signUpShouldReturnFound();
        SignInCommand signInCommand = new SignInCommand(null, null);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/sign-in")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", signInCommand.username())
                        .param("password", signInCommand.password()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/sign-in"));
    }

    @Test
    @DisplayName("계정 인증 처리합니다. 성공 시 302 응답코드를 반환합니다.")
    void signInShouldReturnFound() throws Exception {
        // Given
        signUpShouldReturnFound();
        SignInCommand signInCommand = new SignInCommand("tester", "12341234");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/sign-in")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", signInCommand.username())
                        .param("password", signInCommand.password()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    @DisplayName("계정 인증 해제 처리합니다. 성공 시 302 응답코드를 반환합니다.")
    @WithMockUser
    void signOutShouldReturnFound() throws Exception {
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/sign-out")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/sign-in?logout"));
    }

}