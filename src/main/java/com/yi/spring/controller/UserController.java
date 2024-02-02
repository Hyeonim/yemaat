package com.yi.spring.controller;

import com.yi.spring.entity.QA;
import com.yi.spring.entity.Reservation;
import com.yi.spring.entity.Review;
import com.yi.spring.entity.User;
import com.yi.spring.repository.QARepository;
import com.yi.spring.repository.ReservationRepository;
import com.yi.spring.repository.ReviewRepository;
import com.yi.spring.repository.UserRepository;
import com.yi.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("user/")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    QARepository qaRepository;

//    @GetMapping("my_page")
//    public String userPage(Model model){
//
//        return "userPage/userPage";
//    }

    @GetMapping("user_content/{userNo}")
    public String userPageForm(@PathVariable("userNo") int userNo, Model model) {
        model.addAttribute("user", userService.findByUserNo(userNo));
        return "/userPage/user_content";
    }

    @GetMapping("user_posts/{userNo}")
    public String userPosts(@PathVariable("userNo") Long userNo, Model model){
        List<Reservation> list = reservationRepository.findByUserNo(userNo);
        model.addAttribute("list", list);
        return "/userPage/user_posts";
    }

    @GetMapping("user_review/{userNo}")
    public String userReviews(@PathVariable("userNo") User userNo, Model model){
        List<Review> list = reviewRepository.findByUserNo(userNo);
        model.addAttribute("review", list);
        return "/userPage/user_review";
    }

    @GetMapping("user_QA/{userNo}")
    public String userQA(@PathVariable("userNo") User userNo, Model model){
        List<QA> list = qaRepository.findByUserNo(userNo);
        model.addAttribute("QA", list);
        System.out.println(list);
        return "/userPage/user_QA";
    }

    @GetMapping("user_info/{userNo}")
    public String userUpdateForm(@PathVariable("userNo") int userNo, Model model) {
        model.addAttribute("user", userService.findByUserNo(userNo));
        return "/userPage/user_info";
    }
    @ResponseBody
    @PostMapping("user_update/{userNo}")
    public String userUpdate(@PathVariable("userNo") int userNo, @RequestParam MultipartFile file, User users
    ) throws IOException {

        userRepository.save(users);

        Optional<User> userOptional = userRepository.findByUserNo(userNo);
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

        return "userPage/user_content";
    }




    @GetMapping("list_user")
    public String userList(Model model) {
        List<User> list = userService.getAllUsers();
        model.addAttribute("userList", list);
        return "user/user_list";
    }


    @GetMapping("add_user_form")
    public String userAddForm() {
        return "user/user_add_form";
    }

    @ResponseBody
    @PostMapping("add_user")
    public String userAdd(@RequestParam MultipartFile file, User user) {
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

        return "redirect:/user/list_user";
    }


    @Transactional
    @GetMapping("delete_user/{userNo}")
    public String userDelete(@PathVariable("userNo") int userNo) {
        userService.deleteByUserNo(userNo);
        return "redirect:/user/list_user";
    }

//    @GetMapping("/file-spring")
//    public String getSpringFileForm() {
//        return "user/file";
//    }



}