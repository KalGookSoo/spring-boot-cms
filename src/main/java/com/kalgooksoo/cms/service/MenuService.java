package com.kalgooksoo.cms.service;

import com.kalgooksoo.cms.command.CreateMenuCommand;
import com.kalgooksoo.cms.command.UpdateMenuCommand;
import com.kalgooksoo.cms.entity.Menu;
import org.springframework.lang.NonNull;

import java.util.List;

public interface MenuService {
    Menu create(@NonNull CreateMenuCommand command);
    List<Menu> findAll();
    Menu find(@NonNull String id);
    Menu update(@NonNull String id, @NonNull UpdateMenuCommand command);
    void delete(@NonNull String id);
}
