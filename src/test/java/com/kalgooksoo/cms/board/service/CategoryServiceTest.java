package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.entity.CategoryType;
import com.kalgooksoo.cms.board.repository.CategoryRepository;
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
class CategoryServiceTest {

    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {
        categoryService = new DefaultCategoryService(categoryRepository);
    }

    @Test
    @DisplayName("카테고리 생성 - 카테고리 반환")
    void createShouldReturnCategory() {
        // Given
        CreateCategoryCommand command = new CreateCategoryCommand(null, "카테고리", CategoryType.PUBLIC);

        // When
        Category createdCategory = categoryService.create(command);
        entityManager.flush();

        // Then
        assertNotNull(createdCategory.getId());
        assertEquals("카테고리", createdCategory.getName());
        assertEquals(CategoryType.PUBLIC, createdCategory.getType());
    }

    @Test
    @DisplayName("카테고리 생성 - 카테고리 & 부모 카테고리 반환")
    void createShouldReturnCategoryWithParent() {
        // Given
        CreateCategoryCommand createParentCategoryCommand = new CreateCategoryCommand(null, "부모카테고리", CategoryType.PUBLIC);
        Category createdParentCategory = categoryService.create(createParentCategoryCommand);
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand(createdParentCategory.getId(), "부모카테고리", CategoryType.PUBLIC);

        // When
        Category createdCategory = categoryService.create(createCategoryCommand);
        entityManager.flush();

        // Then
        assertEquals(createdParentCategory.getId(), createdCategory.getParentId());
    }

    @Test
    @DisplayName("카테고리 수정 - 수정된 카테고리 반환")
    void updateShouldReturnCategory() {
        // Given
        CreateCategoryCommand createParentCategoryCommand = new CreateCategoryCommand(null, "부모카테고리", CategoryType.PUBLIC);
        Category createdParentCategory = categoryService.create(createParentCategoryCommand);
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand(createdParentCategory.getId(), "카테고리", CategoryType.PUBLIC);
        Category createdCategory = categoryService.create(createCategoryCommand);
        entityManager.flush();
        UpdateCategoryCommand updateCategoryCommand = new UpdateCategoryCommand(null, "수정된 카테고리", CategoryType.PRIVATE);

        // When
        Category updatedCategory = categoryService.update(createdCategory.getId(), updateCategoryCommand);
        entityManager.flush();

        // Then
        assertNull(updatedCategory.getParentId());
        assertEquals("수정된 카테고리", updatedCategory.getName());
        assertEquals(CategoryType.PRIVATE, updatedCategory.getType());
    }

    @Test
    @DisplayName("카테고리 삭제")
    void delete() {
        // Given
        CreateCategoryCommand createParentCategoryCommand = new CreateCategoryCommand(null, "부모카테고리", CategoryType.PUBLIC);
        Category createdParentCategory = categoryService.create(createParentCategoryCommand);
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand(createdParentCategory.getId(), "카테고리", CategoryType.PUBLIC);
        Category createdCategory = categoryService.create(createCategoryCommand);
        entityManager.flush();
        entityManager.clear();

        // When & Then
        categoryService.delete(createdParentCategory.getId());
        categoryService.delete(createdCategory.getId());
        entityManager.flush();
        assertThrows(NoSuchElementException.class, () -> categoryService.find(createdCategory.getId()));
    }

    @SuppressWarnings("NonAsciiCharacters")
    void initiate() {
        Category 정보공개 = categoryService.create(new CreateCategoryCommand(null, "정보공개", CategoryType.PUBLIC));
        Category 정보공개제도_안내 = categoryService.create(new CreateCategoryCommand(정보공개.getId(), "정보공개제도 안내", CategoryType.PUBLIC));
        Category 개요 = categoryService.create(new CreateCategoryCommand(정보공개제도_안내.getId(), "개요", CategoryType.PUBLIC));
        Category 처리절차 = categoryService.create(new CreateCategoryCommand(정보공개제도_안내.getId(), "처리절차", CategoryType.PUBLIC));
        Category 공개방법 = categoryService.create(new CreateCategoryCommand(정보공개제도_안내.getId(), "공개방법", CategoryType.PUBLIC));
        Category 불복구제절차 = categoryService.create(new CreateCategoryCommand(정보공개제도_안내.getId(), "불복구제절차", CategoryType.PUBLIC));
        Category 비공개_대상_정보 = categoryService.create(new CreateCategoryCommand(정보공개제도_안내.getId(), "비공개 대상 정보", CategoryType.PUBLIC));
        Category 경영공시 = categoryService.create(new CreateCategoryCommand(정보공개.getId(), "경영공시", CategoryType.PUBLIC));
        Category 통합공시 = categoryService.create(new CreateCategoryCommand(경영공시.getId(), "통합공시", CategoryType.PUBLIC));
        Category 자체공시 = categoryService.create(new CreateCategoryCommand(경영공시.getId(), "자체공시", CategoryType.PUBLIC));
        Category 경영공시_소개 = categoryService.create(new CreateCategoryCommand(경영공시.getId(), "경영공시 소개", CategoryType.PUBLIC));
        Category 경영공시_담당자 = categoryService.create(new CreateCategoryCommand(경영공시.getId(), "경영공시 담당자", CategoryType.PUBLIC));
        Category 자주묻는_질문 = categoryService.create(new CreateCategoryCommand(경영공시.getId(), "자주묻는 질문", CategoryType.PUBLIC));
        Category 경영공시_개선센터 = categoryService.create(new CreateCategoryCommand(경영공시.getId(), "경영공시 개선센터", CategoryType.PUBLIC));
        Category 정보목록 = categoryService.create(new CreateCategoryCommand(정보공개.getId(), "정보목록", CategoryType.PUBLIC));
        Category 사전정보공표 = categoryService.create(new CreateCategoryCommand(정보공개.getId(), "사전정보공표", CategoryType.PUBLIC));
        Category 사업실명제 = categoryService.create(new CreateCategoryCommand(정보공개.getId(), "사업실명제", CategoryType.PUBLIC));
        Category 공공데이터 = categoryService.create(new CreateCategoryCommand(정보공개.getId(), "공공데이터", CategoryType.PUBLIC));
        Category 공공데이터_이용안내 = categoryService.create(new CreateCategoryCommand(공공데이터.getId(), "공공데이터 이용안내", CategoryType.PUBLIC));
        Category 공공데이터_활용안내 = categoryService.create(new CreateCategoryCommand(공공데이터.getId(), "공공데이터 활용안내", CategoryType.PUBLIC));
        Category 공공데이터_개방목록 = categoryService.create(new CreateCategoryCommand(공공데이터.getId(), "공공데이터 개방목록", CategoryType.PUBLIC));
        Category 공공데이터_의견제안 = categoryService.create(new CreateCategoryCommand(공공데이터.getId(), "공공데이터 의견제안", CategoryType.PUBLIC));
        Category 정보공개청구 = categoryService.create(new CreateCategoryCommand(정보공개.getId(), "정보공개청구", CategoryType.PUBLIC));

        Category 국민참여 = categoryService.create(new CreateCategoryCommand(null, "국민참여", CategoryType.PUBLIC));
        Category 국민소통 = categoryService.create(new CreateCategoryCommand(국민참여.getId(), "국민소통", CategoryType.PUBLIC));
        Category 정책제안 = categoryService.create(new CreateCategoryCommand(국민소통.getId(), "정책제안", CategoryType.PUBLIC));
        Category 설문참여 = categoryService.create(new CreateCategoryCommand(국민소통.getId(), "설문참여", CategoryType.PUBLIC));
        Category 공모참여 = categoryService.create(new CreateCategoryCommand(국민소통.getId(), "공모참여", CategoryType.PUBLIC));
        Category 국민심사 = categoryService.create(new CreateCategoryCommand(국민소통.getId(), "국민심사", CategoryType.PUBLIC));
        Category 프로그램운영제안 = categoryService.create(new CreateCategoryCommand(국민소통.getId(), "프로그램운영제안", CategoryType.PUBLIC));


        Category 알림홍보 = categoryService.create(new CreateCategoryCommand(null, "알림·홍보", CategoryType.PUBLIC));
        Category 사업안내 = categoryService.create(new CreateCategoryCommand(null, "사업안내", CategoryType.PUBLIC));
        Category 열린경영 = categoryService.create(new CreateCategoryCommand(null, "열린경영", CategoryType.PUBLIC));
        Category 기관소개 = categoryService.create(new CreateCategoryCommand(null, "기관소개", CategoryType.PUBLIC));
//        Category createdParentCategory = categoryService.create(createParentCategoryCommand);
//        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand(createdParentCategory.getId(), "부모카테고리", CategoryType.PUBLIC);

//        Category createdCategory = categoryService.create(createCategoryCommand);
    }

}