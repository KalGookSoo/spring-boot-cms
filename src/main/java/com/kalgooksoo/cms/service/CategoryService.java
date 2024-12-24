package com.kalgooksoo.cms.service;

import com.kalgooksoo.cms.command.CreateCategoryCommand;
import com.kalgooksoo.cms.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.entity.Category;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CategoryService {
    Category create(@NonNull CreateCategoryCommand command);
    List<Category> findAll();
    Category find(@NonNull String id);
    Category update(@NonNull String id, @NonNull UpdateCategoryCommand command);
    void delete(@NonNull String id);
}
