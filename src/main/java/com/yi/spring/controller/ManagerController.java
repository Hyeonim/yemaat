package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.User;
import com.yi.spring.service.UserService;
import com.yi.spring.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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


//    private final UserServiceImpl userService;
//
//    public ManagerController(UserServiceImpl userService) {
//        this.userService = userService;
//    }
    @Autowired
    UserService userService;


    // 나
    @GetMapping("/{subPage}")
    public String managerPage(Model model, @PathVariable String subPage) {
        model.addAttribute( "page", "managerPage/"+subPage );
        return "managerPage";
    }

    // 나
    @GetMapping("/managerInfo")
    public String managerInfoA(Model model) {

        model.addAttribute( "page", "managerPage/managerInfo");

        List<User> userList = userService.getAllUsers();

        List<User> onlyjum = new ArrayList<>();
        for (User result : userList) {
            if (result.getUserAuth().equals("2")) {
                onlyjum.add(result);
            }
        }
        model.addAttribute("userList", onlyjum);

        return "managerPage";
    }
}
