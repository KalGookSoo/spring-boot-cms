package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateMenuCommand;
import com.kalgooksoo.cms.board.command.UpdateMenuCommand;
import com.kalgooksoo.cms.board.entity.Menu;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

public interface MenuService {
    void refresh();
    LocalDateTime getRefreshTime();
    Menu create(@NonNull CreateMenuCommand command);
    List<Menu> findAll();
    Menu find(@NonNull String id);
    Menu update(@NonNull String id, @NonNull UpdateMenuCommand command);
    void delete(@NonNull String id);
}
