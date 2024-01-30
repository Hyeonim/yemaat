package com.yi.spring.controller;

import com.yi.spring.entity.DiningRest;
import com.yi.spring.entity.Dinning;
import com.yi.spring.service.DiningRestService;
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

    @GetMapping("viewRest/{restNo}")
    public String viewRest(@PathVariable("restNo") int restNo, Model model) {
        Dinning dinning = diningRestService.getRestByRestNo(restNo);
        model.addAttribute("dinning", dinning);
        return "myPage/viewRest";
    }
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
