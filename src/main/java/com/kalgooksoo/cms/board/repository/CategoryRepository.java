package com.kalgooksoo.cms.board.repository;

import com.kalgooksoo.cms.board.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
