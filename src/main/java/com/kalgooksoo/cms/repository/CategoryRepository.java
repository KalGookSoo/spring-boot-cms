package com.kalgooksoo.cms.repository;

import com.kalgooksoo.cms.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
