package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import org.springframework.lang.NonNull;

import java.util.Collection;

public interface CategoryService {
    Category create(@NonNull CreateCategoryCommand command);
    Collection<Category> findAllNested();
    Collection<Category> findAll();
    Category find(@NonNull String id);
    Category update(@NonNull String id, @NonNull UpdateCategoryCommand command);
    void delete(@NonNull String id);
}
