package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class ManagerController {


//    @GetMapping("/")
//    public String manager(Model model) {
//        return "managerPage";
//    }

    @GetMapping("/{subPage}")
    public String managerPage(Model model, @PathVariable String subPage) {
        model.addAttribute( "page", "managerPage/"+subPage );
        return "managerPage";
    }

//    @GetMapping("/managerInfoPage")
//    public String managerInfoPage(Model model) {
//        // 필요한 로직 추가
//        return "managerInfoPage"; // managerInfoPage.html을 반환
//    }

}
