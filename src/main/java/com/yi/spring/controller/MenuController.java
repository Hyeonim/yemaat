package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Menu;
import com.yi.spring.service.DiningRestService;
import com.yi.spring.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/menu/*")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private DiningRestService diningRestService;

    @GetMapping("listMenu/{restNo}")
    public String listMenu(@PathVariable("restNo") int restNo, Model model) {
        List<Menu> menuList = menuService.getMenusByRestNo(restNo);
        model.addAttribute("menuList", menuList);
        model.addAttribute("restNo", restNo);
        model.addAttribute("menuPage", "/menu/listMenu");
        return "redirect:/myPage/viewRest/"+restNo;
    }

    @GetMapping("addMenu/{restNo}")
    public String createMenuForm(@PathVariable("restNo")int restNo, Model model) {
        Dinning dinning = diningRestService.getRestByRestNo(restNo);
        model.addAttribute("restNo", dinning.getRestNo());
        model.addAttribute("menuPage", "/menu/addMenu");
        return "redirect:/myPage/viewRest/"+restNo;
    }
    @PostMapping("addMenu/{restNo}")
    public String createMenu(@PathVariable("restNo")int restNo, Menu menu) {
        menu.setRestNo(new Dinning(restNo));
        Menu savedMenu = menuService.createMenu(menu);
        System.out.println(menu);
        return "menu/listMenu";
    }

    @GetMapping("updateMenu/{menuNo}")
    public String updateMenu(@PathVariable("menuNo") int menuNo, Model model) {
        Menu menu = menuService.getMenuByMenuNo(menuNo);
        model.addAttribute("menu", menu);
        return "menu/updateMenu";
    }

    @PostMapping("updateMenu/{menuNo}")
    public String updateMenu(@PathVariable("menuNo") int menuNo, Menu Menu) {
        Menu.setId(menuNo);
        Menu updateMenu = menuService.updateMenu(Menu);
        return "menu/listMenu";
    }
    @GetMapping("deleteMenu/{menuNo}")
    public String deleteMenu(@PathVariable("menuNo") int menuNo) {
        menuService.deleteMenu(menuNo);
        return "menu/listMenu";
    }

}
