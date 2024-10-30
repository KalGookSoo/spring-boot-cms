package com.kalgooksoo.cms.board.repository;

import com.kalgooksoo.cms.board.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, String> {
}
