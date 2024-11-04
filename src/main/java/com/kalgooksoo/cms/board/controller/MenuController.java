package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.HierarchicalFactory;
import com.kalgooksoo.cms.board.entity.Menu;
import com.kalgooksoo.cms.board.service.MenuService;
import com.kalgooksoo.core.excel.ExcelWriter;
import com.kalgooksoo.core.excel.ExcelXlsxView;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.Map;

@Tag(name = "MenuController", description = "메뉴 컨트롤러")
@Controller
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public String getMenus(Model model) {
        // Query
        List<Menu> menus = menuService.findAll();

        // Model
        model.addAttribute("refreshTime", menuService.getRefreshTime());
        model.addAttribute("menus", HierarchicalFactory.build(menus));

        // View
        return "menus/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/excel-export")
    public View exportExcel(Model model) {
        // Query
//        List<Menu> menus = menuService.findAll();
//        Map<String, Object> excelData = ExcelWriter.createExcelData(menus, Menu.class);

        // Model
//        model.addAllAttributes(excelData);

        // View
        return new ExcelXlsxView();
    }

}
