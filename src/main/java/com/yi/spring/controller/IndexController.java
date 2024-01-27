package com.yi.spring.controller;

import com.yi.spring.entity.User;
import com.yi.spring.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/*")
public class IndexController {

    @Autowired
    UserService userService;

    @GetMapping("/home")
    public String index(Model model, HttpSession httpSession) {

//
//        model.addAttribute("data", "손흥민");

//        Optional<User> user = userService.getUserEmail("b1");
//        model.addAttribute("user", user);
//        User user = userService.getUserEmail("c.com");
//        model.addAttribute("user", user);
//        httpSession.setAttribute("sessionData", "나는 세션");
//        model.addAttribute("localDateTime", LocalDateTime.now());

//        List<User> list = userService.getAllUsers();
//        System.out.println(list);
//        model.addAttribute("list", list);
//        model.addAttribute("hi", "나는야퉁퉁이");

        return "main";
    }

    @GetMapping("/")
    public String home(Model model, HttpSession httpSession) {

//
//        model.addAttribute("data", "손흥민");

//        Optional<User> user = userService.getUserEmail("b1");
//        model.addAttribute("user", user);
//        User user = userService.getUserEmail("c.com");
//        model.addAttribute("user", user);
//        httpSession.setAttribute("sessionData", "나는 세션");
//        model.addAttribute("localDateTime", LocalDateTime.now());

//        List<User> list = userService.getAllUsers();
//        System.out.println(list);
//        model.addAttribute("list", list);
//        model.addAttribute("hi", "나는야퉁퉁이");

        return "index";
    }


    @Component("helloBean")
    static class helloBean{
        public String hello(String data){
            return "안녕" + data;
        }
    }


    @PostMapping("/submit")
    public String submitForm(@RequestParam String email, Model model) {


        Optional<User> user = userService.getUserEmail(email);
        model.addAttribute("user", user);

//        model.addAttribute("user", user);

        return "index";
    }
}
