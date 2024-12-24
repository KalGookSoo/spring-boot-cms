package com.kalgooksoo.cms.repository;

import com.kalgooksoo.cms.entity.User;
import com.kalgooksoo.cms.repository.UserPrincipalRepository;
import com.kalgooksoo.cms.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserPrincipalRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPrincipalRepository userPrincipalRepository;

    @Test
    @DisplayName("계정명에 해당하는 계정을 조회합니다. 성공 시 계정을 반환합니다.")
    void findByUsernameTest() {
        // Given
        User user = User.create("tester", "12345678", "테스터", null, null);
        User savedUser = userRepository.save(user);

        // When
        Optional<User> foundUser = userPrincipalRepository.findByUsername(savedUser.getUsername());

        // Then
        assertTrue(foundUser.isPresent());
    }

    @Test
    @DisplayName("계정명에 해당하는 계정을 조회합니다. 존재하지 않는 계정 조회 시 빈 Optional을 반환합니다.")
    void findByUsernameShouldReturnEmptyOptional() {
        // Given
        String username = "tester";

        // When
        Optional<User> foundUser = userPrincipalRepository.findByUsername(username);

        // Then
        assertTrue(foundUser.isEmpty());
    }

}