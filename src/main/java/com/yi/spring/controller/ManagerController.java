package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class ManagerController {


    @GetMapping("manager")
    public String manager(Model model) {



        return "managerPage";
    }

}
