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
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private DiningRestService diningRestService;

    @GetMapping("{restNo}")
//    public ResponseEntity<List<Menu>> getMenusByRestNo(@PathVariable("restNo") Integer restNo) {
    public String getMenusByRestNo(Model model, @PathVariable("restNo") Integer restNo) {
        List<Menu> menuList = menuService.getMenusByRestNo(restNo);

        model.addAttribute( "list", menuList );
        return "menu/list";
//        return new ResponseEntity<>(menuList, HttpStatus.OK);
    }

    @GetMapping("/addMenu")
    public String createMenuForm(Model model) {
        Dinning dinning = diningRestService.getRestByRest_no(181);
        model.addAttribute("restNo", dinning.getRestNo());
        return "menu/addMenu";
    }
    @PostMapping
    public ResponseEntity<Menu> createMenu(@RequestBody Menu menu) {
        Menu savedMenu = menuService.createMenu(menu);
        return new ResponseEntity<>(savedMenu, HttpStatus.CREATED);
    }

    @PutMapping("{menuNo}")
    public ResponseEntity<Menu> updateMenu(@PathVariable("menuNo") Integer menuNo, @RequestBody Menu Menu) {
        Menu.setMenuNo(menuNo);
        Menu updateMenu = menuService.updateMenu(Menu);
        return new ResponseEntity<>(updateMenu, HttpStatus.OK);
    }

    @DeleteMapping("{menuNo}")
    public  ResponseEntity<String> deleteMenu(@PathVariable("menuNo") Integer menuNo) {
        menuService.deleteMenu(menuNo);
        return new ResponseEntity<>("Menu successfully delete!", HttpStatus.OK);
    }
}
