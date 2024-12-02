package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateMenuCommand;
import com.kalgooksoo.cms.board.command.UpdateMenuCommand;
import com.kalgooksoo.cms.board.entity.Menu;
import com.kalgooksoo.cms.board.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MenuServiceTest {

    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {
        menuService = new DefaultMenuService(menuRepository);
    }

    @Test
    @DisplayName("메뉴 생성 - 메뉴 반환")
    void createShouldReturnMenu() {
        // Given
        CreateMenuCommand command = new CreateMenuCommand(null, "메뉴", "/", 1);

        // When
        Menu createdMenu = menuService.create(command);
        entityManager.flush();

        // Then
        assertNotNull(createdMenu.getId());
        assertEquals("메뉴", createdMenu.getName());
        assertEquals("/", createdMenu.getUri());
        assertEquals(1, createdMenu.getSequence());
    }

    @Test
    @DisplayName("메뉴 생성 - 메뉴 & 부모 메뉴 반환")
    void createShouldReturnMenuWithParent() {
        // Given
        CreateMenuCommand createParentMenuCommand = new CreateMenuCommand(null, "메뉴", "/", 1);
        Menu createdParentMenu = menuService.create(createParentMenuCommand);
        CreateMenuCommand createMenuCommand = new CreateMenuCommand(createdParentMenu.getId(), "메뉴", "/", 1);

        // When
        Menu createdMenu = menuService.create(createMenuCommand);
        entityManager.flush();

        // Then
        assertEquals(createdParentMenu, createdMenu);
    }

    @Test
    @DisplayName("메뉴 수정 - 수정된 메뉴 반환")
    void updateShouldReturnMenu() {
        // Given
        CreateMenuCommand createParentMenuCommand = new CreateMenuCommand(null, "메뉴", "/", 1);
        Menu createdParentMenu = menuService.create(createParentMenuCommand);
        CreateMenuCommand createMenuCommand = new CreateMenuCommand(createdParentMenu.getId(), "메뉴", "/", 1);
        Menu createdMenu = menuService.create(createMenuCommand);
        entityManager.flush();
        UpdateMenuCommand updateMenuCommand = new UpdateMenuCommand(null, "수정된 메뉴", "/api", 2);

        // When
        Menu updatedMenu = menuService.update(createdMenu.getId(), updateMenuCommand);
        entityManager.flush();

        // Then
        assertNull(updatedMenu.getParent());
        assertEquals("수정된 메뉴", updatedMenu.getName());
        assertEquals("/api", createdMenu.getUri());
        assertEquals(2, createdMenu.getSequence());
    }

    @Test
    @DisplayName("메뉴 삭제")
    void delete() {
        // Given
        CreateMenuCommand createParentMenuCommand = new CreateMenuCommand(null, "메뉴", "/", 1);
        Menu createdParentMenu = menuService.create(createParentMenuCommand);
        CreateMenuCommand createMenuCommand = new CreateMenuCommand(null, "메뉴", "/", 1);
        Menu createdMenu = menuService.create(createMenuCommand);
        entityManager.flush();
        entityManager.clear();

        // When & Then
        menuService.delete(createdParentMenu.getId());
        menuService.delete(createdMenu.getId());
        entityManager.flush();
        assertThrows(NoSuchElementException.class, () -> menuService.find(createdMenu.getId()));
    }

}
