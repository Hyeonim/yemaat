package com.yi.spring.controller;

import com.yi.spring.entity.User;
import com.yi.spring.repository.UserRepository;
import com.yi.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class ManagerController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/{subPage}")
    public String managerPage(Model model, @PathVariable String subPage) {
        model.addAttribute( "page", "managerPage/"+subPage );

        return "managerPage";
    }

//    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ유저꺼ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @GetMapping("/managerPage_UList")
    public String managerListU(Model model) {


        List<User> users = userRepository.findAll();
        List<User> onlyUsers = new ArrayList<>();
        for (User result : users) {
            if (result.getUserAuth().equals("1")) {
                onlyUsers.add(result);
            }
        }
        model.addAttribute("users",onlyUsers);


        model.addAttribute( "page", "managerPage/managerPage_UList" );
        return "managerPage";
    }
//
//
//    @PostMapping("managerPage_UAdd")
//    public String userAdd(@RequestParam MultipartFile file, User user,Model model) {
//
//        model.addAttribute( "page", "managerPage/managerPage_UList" );
//
//        if (file.isEmpty()) {
//            userRepository.save(user);
//        } else {
//            byte[] userImg = new byte[0];
//            try {
//                userImg = file.getBytes();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            user.setUserImg(userImg);
//        }
//
//        userRepository.save(user);
//
//        return "/managerPage";
//    }



//    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ점주꺼ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @GetMapping("/managerPage_JInfo")
    public String managerInfoA(Model model) {

        model.addAttribute( "page", "managerPage/managerPage_JInfo");

        List<User> userList = userService.getAllUsers();

        List<User> Owner = new ArrayList<>();
        for (User result : userList) {
            if (result.getUserAuth().equals("2")) {
                Owner.add(result);
            }
        }
        model.addAttribute("userList", Owner);

        return "managerPage";
    }
}
