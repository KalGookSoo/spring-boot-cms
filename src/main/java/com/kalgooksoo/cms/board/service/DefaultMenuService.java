package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateMenuCommand;
import com.kalgooksoo.cms.board.command.UpdateMenuCommand;
import com.kalgooksoo.cms.board.entity.Menu;
import com.kalgooksoo.cms.board.repository.MenuRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DefaultMenuService implements MenuService {

    private LocalDateTime refreshTime = LocalDateTime.now();

    private final List<Menu> menus = new ArrayList<>();

    private final MenuRepository menuRepository;

    @PostConstruct
    public void init() {
        refresh();
    }

    @Cacheable("menus")
    @Transactional(readOnly = true)
    @Override
    public void refresh() {
        this.menus.clear();
        List<Menu> menus = menuRepository.findAll(Sort.by(Sort.Direction.ASC, "sequence"));
        this.menus.addAll(menus);
        refreshTime = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getRefreshTime() {
        return refreshTime;
    }

    @Override
    public Menu create(@NonNull CreateMenuCommand command) {
        Menu menu = Menu.create(command);
        Menu saved = menuRepository.save(menu);
        refresh();
        return saved;
    }

    @Cacheable("menus")
    @Transactional(readOnly = true)
    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public Menu find(@NonNull String id) {
        return menuRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Menu update(@NonNull String id, @NonNull UpdateMenuCommand command) {
        Menu menu = menuRepository.getReferenceById(id);
        menu.update(command);
        Menu saved = menuRepository.save(menu);
        refresh();
        return saved;
    }

    @Transactional
    @Override
    public void delete(@NonNull String id) {
        menuRepository.deleteById(id);
        refresh();
    }
}
