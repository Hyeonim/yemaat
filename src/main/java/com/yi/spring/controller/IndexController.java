package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Review;
import com.yi.spring.repository.DinningRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
DinningRepository dinningRepository;



    @GetMapping("/home")
    public String index(Model model, HttpSession httpSession) {

        long a = 181; //restNo

        Optional<Dinning> dinningOptional = dinningRepository.findById((a));

        dinningOptional.ifPresent(dinning -> model.addAttribute("dinning", dinning));




        return "main";
    }

    @GetMapping("/login")
    public String loginPG(Model model, HttpSession httpSession){
        return "/login";
    }

    @GetMapping("/usersignUp")
    public String usersignUp(Model model, HttpSession httpSession){
        return "usersignUp";
    }

    @GetMapping("/hostsignUp")
    public String hostsignUp(Model model, HttpSession httpSession){
        return "hostsignUp";
    }

    @GetMapping("/signUp")
    public String signUp(Model model, HttpSession httpSession){
        return "signUp";
    }

    @GetMapping("/restHost")
    public String host(Model model, HttpSession httpSession){
        return "restHost";
    }


}