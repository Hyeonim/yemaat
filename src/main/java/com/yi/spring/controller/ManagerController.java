package com.yi.spring.controller;

import com.yi.spring.entity.User;
import com.yi.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class ManagerController {

    @Autowired
    UserService userService;

    @GetMapping("/{subPage}")
    public String managerPage(Model model, @PathVariable String subPage) {

        model.addAttribute( "page", "managerPage/"+subPage );


        List<User> users = userService.getAllUsers();
        System.out.println(users);
        model.addAttribute("users",users);

        return "managerPage";
    }

    @GetMapping("/aa/managerPage_UList")
    public String managerUList(Model model, @PathVariable String subPage) {

        model.addAttribute( "page", "managerPage/"+subPage );


        List<User> users = userService.getAllUsers();
        System.out.println(users);
        model.addAttribute("users",users);

        return "managerPage";

    }





}
