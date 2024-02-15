package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Review;
import com.yi.spring.entity.User;
import com.yi.spring.repository.DinningRepository;
import com.yi.spring.repository.ReviewRepository;
import com.yi.spring.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    DinningRepository dinningRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String home() {

        return "redirect:/home";
    }
    @GetMapping("/home")
    public String index(Model model, HttpSession httpSession) {
        List<Dinning> list = dinningRepository.getRandomList("5");
        model.addAttribute("dinning", list);

        List<Review> reviewList = new ArrayList<>();
        for ( int i = 0; i < 2; i++ )
            reviewList.addAll( reviewRepository.getRandomList( "10" ) );

        model.addAttribute("revList1", reviewList.subList(0, 10) );
        model.addAttribute("revList2", reviewList.subList(5, 15) );
        model.addAttribute("revList3", reviewList.subList(10, 20) );

        return "main";
    }

    @GetMapping("/homeSlide")
    public String homeSlide(Model model){
        List<Review> reviewList = reviewRepository.getRandomList( "10" );
        model.addAttribute("list", reviewList );
        return "/include/detail_review_template";
    }

    @GetMapping("/login")
    public String loginPG(Model model, HttpSession httpSession) {
        return "/login";
    }

    @GetMapping("/signUp")
    public String usersignUp(Model model, HttpSession httpSession) {
        return "usersignUp";
    }


    @PostMapping("join")
    public String userjoin(@RequestParam MultipartFile file, User user, @RequestParam String userType) {

        user.setUserAuth( "2".equals(userType) ? "2" : "1" );

        if ( !file.isEmpty() ) {
            try {
                byte[] userImg = file.getBytes();
                user.setUserImg(userImg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        userRepository.save(user);

        return "redirect:/login";
    }

//    @GetMapping("/hostsignUp")
//    public String hostsignUp(Model model, HttpSession httpSession) {
//        return "hostsignUp";
//    }

//    @GetMapping("/signUp")
//    public String signUp(Model model, HttpSession httpSession) {
//        return "signUp";
//    }

    @GetMapping("/restHost")
    public String host(Model model, HttpSession httpSession) {
        return "restHost";
    }

    @GetMapping("addRest")
    public String addRest() {
        return "myPage/addRest";
    }
}