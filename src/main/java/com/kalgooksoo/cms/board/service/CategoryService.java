package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;

import java.util.List;

public interface CategoryService {
    Category create(CreateCategoryCommand command);
    List<Category> findAll();
    Category find(String id);
    Category update(String id, UpdateCategoryCommand command);
    void delete(String id);
}
