package com.yi.spring.controller;

import com.yi.spring.entity.User;
import com.yi.spring.repository.UserRepository;
import com.yi.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class ManagerController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/{subPage}")
    public String managerPage(Model model, @PathVariable String subPage) {
        model.addAttribute("page", "managerPage/" + subPage);

        return "managerPage";
    }


    @GetMapping("/managerPage_UDetail")
    public String managerDetailU(Model model, @RequestParam int userNo) {

        System.out.println(userNo);

        Optional<User> user = userRepository.findByUserNo(userNo);
//        System.out.println(user);

        model.addAttribute("user", user);
        model.addAttribute("page", "managerPage/managerPage_UDetail");

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
        model.addAttribute("users", onlyUsers);

        model.addAttribute("page", "managerPage/managerPage_UList");
        return "managerPage";
    }


    @PostMapping("managerPage_UAdd")
    public String managerAddU(@RequestParam MultipartFile file, User user, Model model) {
        if (file.isEmpty()) {
            userRepository.save(user);
        } else {
            byte[] userImg = new byte[0];
            try {
                userImg = file.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            user.setUserImg(userImg);
        }

        userRepository.save(user);

        return "redirect:/managerPage_UList";
    }

    @ResponseBody
    @GetMapping("managerPage_UUpdate")
    public String userUpdate(
                             @RequestParam MultipartFile file,
                             User users) throws IOException {


        userRepository.save(users);

        Optional<User> userOptional = userRepository.findByUserNo(users.getUserNo());


        userOptional.ifPresent(user -> {
            byte[] userImg = new byte[0];
            try {
                userImg = file.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            user.setUserImg(userImg);
            userRepository.save(user);
        });


        return "redirect:/managerPage_UList";

    }


//    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ점주꺼ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @GetMapping("/managerPage_JInfo")
    public String managerInfoA(Model model) {

        model.addAttribute("page", "managerPage/managerPage_JInfo");

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

    // 수정 중
//    @PutMapping("/managerPage_JUpd")
//    public String managerUpdJ(Model model){
//        model.addAttribute("page", "managerPage/manager_JUpd");
//    }
}