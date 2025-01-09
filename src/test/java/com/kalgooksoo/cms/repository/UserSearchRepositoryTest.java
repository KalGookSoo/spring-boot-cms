package com.kalgooksoo.cms.repository;


import com.kalgooksoo.cms.entity.User;
import com.kalgooksoo.cms.search.UserSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class UserSearchRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserSearchRepository userSearchRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {
        userSearchRepository = new DefaultUserRepository(entityManager.getEntityManager());
    }

    @Test
    @DisplayName("검색 조건에 기반한 계정 목록을 조회합니다. 성공 시 계정 목록을 반환합니다.")
    void searchTest() {
        // Given
        User user1 = User.create("tester1", "12345678", "테스터1", null, null);
        User user2 = User.create("tester2", "12345678", "테스터1", null, null);
        User user3 = User.create("tester3", "12345678", "테스터1", null, null);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        UserSearch search = new UserSearch();
        search.setUsername("tester3");
        search.setPage(0);
        search.setSize(10);
        search.setSort("createdDate");
        search.setSortDirection("desc");

        // When
        Page<User> page = userSearchRepository.searchAll(search);

        // Then
        List<User> users = page.getContent();
        assertEquals(1, users.size());
    }

    @Test
    @DisplayName("검색 조건에 기반한 계정 목록을 조회합니다. 실패 시 빈 목록을 반환합니다.")
    void searchTestWithEmpty() {
        // Given
        UserSearch search = new UserSearch();
        search.setUsername("tester3");
        search.setPage(0);
        search.setSize(10);
        search.setSort("createdDate");
        search.setSortDirection("desc");
        search.setEmail("tester@test.co.kr");
        search.setContactNumber("01012341234");

        // When
        Page<User> page = userSearchRepository.searchAll(search);

        // Then
        assertTrue(page.isEmpty());
    }

}