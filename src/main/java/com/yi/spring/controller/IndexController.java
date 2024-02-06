package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Review;
import com.yi.spring.repository.DinningRepository;
import com.yi.spring.repository.ReviewRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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


    @GetMapping("/home")
    public String index(Model model, HttpSession httpSession) {

        List<Dinning> list = dinningRepository.findAll();

        List<Dinning> imgList = new ArrayList<>();

        for (Dinning diningRest : list) {
            Dinning elem = new Dinning();
            if (diningRest.getRestImg() != null) {
                elem.setRestImg(diningRest.getRestImg());
                imgList.add(elem);
            }
        }
        model.addAttribute("dinning", imgList);




        List<Review> reviewList = new ArrayList<>();
        for ( int i = 0; i < 15; i++ )
            reviewList.add( reviewRepository.getRandomOne() );
        reviewList.addAll(reviewList.subList(0, 5));

        model.addAttribute("revList1", reviewList.subList(0, 10) );
        model.addAttribute("revList2", reviewList.subList(5, 15) );
        model.addAttribute("revList3", reviewList.subList(10, 20) );



        return "main";

    }

    @GetMapping("/login")
    public String loginPG(Model model, HttpSession httpSession) {
        return "/login";
    }

    @GetMapping("/usersignUp")
    public String usersignUp(Model model, HttpSession httpSession) {
        return "usersignUp";
    }

//    @GetMapping("/hostsignUp")
//    public String hostsignUp(Model model, HttpSession httpSession) {
//        return "hostsignUp";
//    }

    @GetMapping("/signUp")
    public String signUp(Model model, HttpSession httpSession) {
        return "signUp";
    }

    @GetMapping("/restHost")
    public String host(Model model, HttpSession httpSession) {
        return "restHost";
    }

    @GetMapping("addRest")
    public String addRest() {
        return "myPage/addRest";
    }
}