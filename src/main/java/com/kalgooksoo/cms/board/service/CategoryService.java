package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CategoryService {
    Category create(@NonNull CreateCategoryCommand command);
    List<Category> findAll();
    Category find(@NonNull String id);
    Category update(@NonNull String id, @NonNull UpdateCategoryCommand command);
    void delete(@NonNull String id);
}
