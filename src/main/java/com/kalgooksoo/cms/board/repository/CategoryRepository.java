package com.kalgooksoo.cms.board.repository;

import com.kalgooksoo.cms.board.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
