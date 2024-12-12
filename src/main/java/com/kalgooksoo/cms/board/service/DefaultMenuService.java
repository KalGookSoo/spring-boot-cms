package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateMenuCommand;
import com.kalgooksoo.cms.board.command.UpdateMenuCommand;
import com.kalgooksoo.cms.board.entity.Menu;
import com.kalgooksoo.cms.board.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DefaultMenuService implements MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    @Override
    public Menu create(@NonNull CreateMenuCommand command) {
        Menu parent = command.parentId() == null ? null : menuRepository.getReferenceById(command.parentId());
        Menu menu = Menu.create(command, parent);
        return menuRepository.save(menu);
    }

//    @Cacheable("menus")
    @Transactional(readOnly = true)
    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll(Sort.by(Sort.Direction.ASC, "sequence"));
    }

    @Override
    public Menu find(@NonNull String id) {
        return menuRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    @Override
    public Menu update(@NonNull String id, @NonNull UpdateMenuCommand command) {
        Menu menu = menuRepository.getReferenceById(id);
        Menu parent = command.parentId() == null ? null : menuRepository.getReferenceById(command.parentId());
        menu.update(command, parent);
        return menuRepository.save(menu);
    }

    @Transactional
    @Override
    public void delete(@NonNull String id) {
        menuRepository.deleteById(id);
    }
}
