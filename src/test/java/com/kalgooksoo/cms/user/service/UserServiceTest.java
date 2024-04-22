package com.kalgooksoo.cms.user.service;

import com.kalgooksoo.cms.user.command.CreateUserCommand;
import com.kalgooksoo.cms.user.command.UpdateUserCommand;
import com.kalgooksoo.cms.user.entity.User;
import com.kalgooksoo.cms.user.repository.UserRepository;
import com.kalgooksoo.cms.user.search.UserSearch;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest
class UserServiceTest {

    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    @BeforeEach
    void setup() {
        userService = new DefaultUserService(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("계정을 생성합니다. 성공시 계정을 반환합니다.")
    void createUserTest() {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", null, null, null, null, null);

        // When
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("12341234");
        User createdUser = userService.createUser(createUserCommand);

        // Then
        assertNotNull(createdUser);
    }

    @Test
    @DisplayName("계정 생성 시 이미 존재하는 아이디를 입력하면 DataIntegrityViolationException 예외를 발생시킵니다.")
    void createUserWithExistingUsernameTest() {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", null, null, null, null, null);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("12341234");
        userService.createUser(createUserCommand);

        CreateUserCommand duplicateCreateUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", null, null, null, null, null);

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.createUser(duplicateCreateUserCommand);
            userRepository.flush();
        });
    }

    @Test
    @DisplayName("계정 생성 시 계정 정책 날짜를 확인합니다.")
    void createUserWithPolicyTest() {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", null, null, null, null, null);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("12341234");

        // When
        User createdUser = userService.createUser(createUserCommand);

        // Then
        LocalDateTime expectedExpiredAt = LocalDate.now().atTime(LocalTime.MIDNIGHT).plusYears(2L);
        LocalDateTime expectedCredentialsExpiredAt = LocalDate.now().atTime(LocalTime.MIDNIGHT).plusDays(180L);
        assertEquals(expectedExpiredAt, createdUser.getExpiredAt());
        assertEquals(expectedCredentialsExpiredAt, createdUser.getCredentialsExpiredAt());
    }

    @Test
    @DisplayName("계정 정보를 업데이트합니다. 성공 시 수정된 계정을 반환합니다.")
    void updateUserTest() throws DataIntegrityViolationException {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", null, null, null, null, null);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("12341234");
        User createdUser = userService.createUser(createUserCommand);

        UpdateUserCommand command = new UpdateUserCommand("테스터 업데이트", "updated", "email.com", "010", "1234", "5678");

        // When
        User updatedUser = userService.update(createdUser.getId(), command);

        // Then
        assertEquals(command.name(), updatedUser.getName());
        assertEquals(command.emailId() + "@" + command.emailDomain(), updatedUser.getEmail().getValue());
        assertEquals(command.firstContactNumber() + "-" + command.middleContactNumber() + "-" + command.lastContactNumber(), updatedUser.getContactNumber().getValue());
    }

    @Test
    @DisplayName("계정을 ID로 찾습니다. 성공 시 계정을 반환합니다.")
    void findByIdTest() throws DataIntegrityViolationException {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", null, null, null, null, null);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("12341234");
        User createdUser = userService.createUser(createUserCommand);
        String id = createdUser.getId();

        // When
        Optional<User> foundUser = userService.findById(id);

        // Then
        assertTrue(foundUser.isPresent());
    }

    @Test
    @DisplayName("계정을 ID로 찾을 때 계정이 존재하지 않으면 빈 Optional을 반환합니다.")
    void findByIdShouldReturnEmptyOptional() {
        // Given
        String id = UUID.randomUUID().toString();

        // When
        Optional<User> foundUser = userService.findById(id);

        // Then
        assertTrue(foundUser.isEmpty());
    }

    @Test
    @DisplayName("페이지 정보를 담은 계정 목록을 조회합니다. 성공 시 계정 목록을 반환합니다.")
    void findAllTest() throws DataIntegrityViolationException {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", null, null, null, null, null);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("12341234");
        userService.createUser(createUserCommand);
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When
        Page<User> page = userService.findAll(pageRequest);

        // Then
        assertFalse(page.isEmpty());
        assertEquals(1, page.getTotalElements());
        assertEquals(0, page.getNumber());
        assertEquals(10, page.getSize());
    }

    @Test
    @DisplayName("페이지 정보를 담은 계정 목록을 조회합니다. 계정이 없을 경우 빈 페이지를 반환합니다.")
    void findAllShouldReturnEmptyPage() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When
        Page<User> page = userService.findAll(pageRequest);

        // Then
        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
    }

    @Test
    @DisplayName("페이지네이션 정보와 검색 조건에 기반한 계정 목록을 조회합니다. 성공 시 계정 목록을 반환합니다.")
    void searchTest() throws DataIntegrityViolationException {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", null, null, null, null, null);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("12341234");
        userService.createUser(createUserCommand);

        PageRequest pageRequest = PageRequest.of(0, 10);
        UserSearch search = new UserSearch();
        search.setUsername("tester");

        // When
        Page<User> page = userService.findAll(search, pageRequest);

        // Then
        assertFalse(page.isEmpty());
        assertEquals(1, page.getTotalElements());
    }

    @Test
    @DisplayName("페이지네이션 정보와 검색 조건에 기반한 계정 목록을 조회합니다. 계정이 없을 경우 빈 페이지를 반환합니다.")
    void searchShouldReturnEmptyPage() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        UserSearch search = new UserSearch();
        search.setUsername("tester");

        // When
        Page<User> page = userService.findAll(search, pageRequest);

        // Then
        assertTrue(page.isEmpty());
        assertEquals(0, page.getTotalElements());
    }


    @Test
    @DisplayName("계정을 ID로 삭제합니다.")
    void deleteByIdTest() throws DataIntegrityViolationException {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", null, null, null, null, null);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("12341234");
        User createdUser = userService.createUser(createUserCommand);
        String id = createdUser.getId();

        // When
        userService.delete(id);

        // Then
        Optional<User> deletedUser = userService.findById(id);
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    @DisplayName("계정을 ID로 삭제할 때 계정이 존재하지 않으면 EntityNotFoundException 예외를 발생시킵니다.")
    void deleteByIdWithNonExistingUserTest() {
        // Given
        String id = UUID.randomUUID().toString();

        // Then
        assertThrows(EntityNotFoundException.class, () -> userService.delete(id));
    }

    @Test
    @DisplayName("계정의 패스워드를 업데이트합니다. 성공 시 수정된 계정을 반환합니다.")
    void updatePasswordTest() throws DataIntegrityViolationException {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", null, null, null, null, null);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("12341234");
        User createdUser = userService.createUser(createUserCommand);
        String id = createdUser.getId();
        String newPassword = "newPassword";

        // When
        userService.updatePassword(id, "12345678", newPassword);

        // Then
        Optional<User> updatedUser = userService.findById(id);
        assertTrue(updatedUser.isPresent());
        assertEquals(id, updatedUser.get().getId());
    }

    @Test
    @DisplayName("계정의 패스워드를 업데이트할 때 기존 패스워드가 일치하지 않으면 IllegalArgumentException 예외를 발생시킵니다.")
    void updatePasswordWithInvalidPasswordTest() throws DataIntegrityViolationException {
        // Given
        CreateUserCommand createUserCommand = new CreateUserCommand("tester", "12341234", "테스터1", null, null, null, null, null);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("12341234");
        User createdUser = userService.createUser(createUserCommand);
        String id = createdUser.getId();
        String newPassword = "newPassword";

        // When
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Then
        assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(id, "invalidPassword", newPassword));
    }

}