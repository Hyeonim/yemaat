package com.yi.spring.controller;

import com.yi.spring.entity.DiningRest;
import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Menu;
import com.yi.spring.service.DiningRestService;
import com.yi.spring.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/myPage/*")
public class DiningRestController {
    @Autowired
    private DiningRestService diningRestService;
    @Autowired
    private MenuService menuService;

    @GetMapping("home")
    public String home(Model model) {
        return "myPage";
    }

    @GetMapping("listRest")
    public String listRest(Model model) {
        List<Dinning> diningRestList = diningRestService.getAllRestaurants();
        model.addAttribute("listRest", diningRestList);
        return "myPage/listRest";
    }
// -------------------------- 가게 상세보기 및 메뉴 관리 -----------------------------------------
    @GetMapping("viewRest/{restNo}")
    public String viewRest(@PathVariable("restNo") int restNo, Model model) {
        Dinning dinning = diningRestService.getRestByRestNo(restNo);
        model.addAttribute("dinning", dinning);

        List<Menu> menuList = menuService.getMenusByRestNo(restNo);
        model.addAttribute("menuList", menuList);
        model.addAttribute("restNo", restNo);

        model.addAttribute("menuPage", "/menu/listMenu");
        return "myPage/viewRest";
    }

    @GetMapping("viewRest/{restNo}/addMenu")
    public String createMenuForm(@PathVariable("restNo")int restNo, Model model) {
        Dinning dinning = diningRestService.getRestByRestNo(restNo);
        model.addAttribute("dinning", dinning);
        model.addAttribute("restNo", dinning.getRestNo());
        model.addAttribute("menuPage", "/menu/addMenu");
        return "/myPage/viewRest";
    }
    @PostMapping("viewRest/{restNo}/addMenu")
    public String createMenu(@PathVariable("restNo")int restNo, Menu menu, Model model) {
        Dinning dinning = diningRestService.getRestByRestNo(restNo);
        model.addAttribute("dinning", dinning);

        menu.setRestNo(dinning);
        Menu savedMenu = menuService.createMenu(menu);

        return "redirect:/myPage/viewRest/"+restNo;
    }

    @GetMapping("viewRest/{restNo}/updateMenu/{menuNo}")
    public String updateMenu(@PathVariable("restNo") int restNo, @PathVariable("menuNo") int menuNo, Model model) {
        Dinning dinning = diningRestService.getRestByRestNo(restNo);
        model.addAttribute("dinning", dinning);

        Menu menu = menuService.getMenuByMenuNo(menuNo);
        model.addAttribute("menu", menu);
        model.addAttribute("menuPage", "/menu/updateMenu");
        return "myPage/viewRest";
    }

    @PostMapping("viewRest/{restNo}/updateMenu/{menuNo}")
    public String updateMenu(@PathVariable("restNo") int restNo, @PathVariable("menuNo") int menuNo, Menu Menu, Model model) {
        Dinning dinning = diningRestService.getRestByRestNo(restNo);
        model.addAttribute("dinning", dinning);

        Menu.setMenuNo(menuNo);
        Menu updateMenu = menuService.updateMenu(Menu);
        return "redirect:/myPage/viewRest/"+restNo;
    }

    @GetMapping("viewRest/{restNo}/deleteMenu/{menuNo}")
    public String deleteMenu(@PathVariable("restNo") int restNo, @PathVariable("menuNo") int menuNo, Model model) {
        Dinning dinning = diningRestService.getRestByRestNo(restNo);
        model.addAttribute("dinning", dinning);
        menuService.deleteMenu(menuNo);
        return "redirect:/myPage/viewRest/"+restNo;
    }

    // ------------------------------------------------------------------------------------------
    @GetMapping("addRest")
    public String addRest(Model model) {
        return "myPage/addRest";
    }

    @PostMapping("addRest")
    public String addRest(Dinning dinning, Model model) {
        System.out.println(dinning);
        diningRestService.createRestaurant(dinning);
        return "myPage/listRest";
    }

    @GetMapping("updateRest/{restNo}")
    public String updateRest(@PathVariable("restNo") int restNo, Model model) {
        Dinning dinning = diningRestService.getRestByRestNo(restNo);
        model.addAttribute("dinning", dinning);
        return "myPage/updateRest";
    }

    @PostMapping("updateRest/{restNo}")
    public String updateRest(@PathVariable("restNo") int restNo, Dinning dinning) {
        dinning.setRestNo(restNo);
        Dinning updateRestaurant = diningRestService.updateRestaurant(dinning);
        return "myPage/listRest";
    }

    @GetMapping("deleteRest/{restNo}")
    public String deleteRest(@PathVariable("restNo") int restNo, Model model) {
        diningRestService.deleteRestaurant(restNo);
        return "myPage/listRest";
    }
}
