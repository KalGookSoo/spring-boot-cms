package com.kalgooksoo.cms.repository;

import com.kalgooksoo.cms.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("계정을 저장합니다. 성공 시 계정을 반환합니다.")
    void saveTest() {
        // Given
        User user = User.create("tester", "12345678", "테스터", null, null);

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertNotNull(savedUser);
    }

    @Test
    @DisplayName("계정을 조회합니다. 성공 시 계정을 반환합니다.")
    void findByIdTest() {
        // Given
        User user = User.create("tester", "12345678", "테스터", null, null);
        User savedUser = userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Then
        assertTrue(foundUser.isPresent());
    }

    @Test
    @DisplayName("계정을 조회합니다. 존재하지 않는 계정 조회 시 빈 Optional을 반환합니다.")
    void findByIdShouldReturnEmptyOptional() {
        // Given
        String id = UUID.randomUUID().toString();

        // When
        Optional<User> foundUser = userRepository.findById(id);

        // Then
        assertTrue(foundUser.isEmpty());
    }

    @Test
    @DisplayName("계정을 삭제합니다. 성공 시 삭제된 계정을 조회할 수 없습니다.")
    void deleteByIdTest() {
        // Given
        User user = User.create("tester", "12345678", "테스터", null, null);
        User savedUser = userRepository.save(user);

        // When
        userRepository.deleteById(savedUser.getId());

        // Then
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    @DisplayName("계정을 삭제합니다. 존재하지 않는 계정 삭제 시 빈 Optional을 던집니다.")
    void deleteByIdShouldReturnOptionalEmpty() {
        // Given
        String id = UUID.randomUUID().toString();

        // When
        userRepository.deleteById(id);

        // Then
        assertTrue(userRepository.findById(id).isEmpty());
    }

    @Test
    @DisplayName("계정 목록을 조회합니다. 성공 시 계정 목록을 반환합니다.")
    void findAllTest() {
        // Given
        User user1 = User.create("tester1", "12345678", "테스터1", null, null);
        User user2 = User.create("tester2", "12345678", "테스터1", null, null);
        User user3 = User.create("tester3", "12345678", "테스터1", null, null);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Sort sort = Sort.by(Sort.Order.desc("createdDate"));
        PageRequest pageable = PageRequest.of(0, 10, sort);

        // When
        Page<User> page = userRepository.findAll(pageable);

        // Then
        List<User> users = page.getContent();
        assertEquals(3, users.size());
    }

    @Test
    @DisplayName("계정 목록을 조회합니다. 실패 시 빈 목록을 반환합니다.")
    void findAllTestWithEmpty() {
        // When
        Page<User> page = userRepository.findAll(PageRequest.of(0, 10));

        // Then
        assertTrue(page.isEmpty());
    }

}