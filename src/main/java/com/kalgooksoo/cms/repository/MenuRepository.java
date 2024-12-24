package com.kalgooksoo.cms.repository;

import com.kalgooksoo.cms.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, String> {
}
