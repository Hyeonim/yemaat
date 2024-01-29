package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Review;
import com.yi.spring.entity.User;
import com.yi.spring.repository.DinningRepository;
import com.yi.spring.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/*")
public class detailController {

    @Autowired
    DinningRepository dinningRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @GetMapping("/detail")
    public String getDinningByRestNo(Model model) {


        long a = 185; //restNo

        Optional<Dinning> dinningOptional = dinningRepository.findById((a));

        List<Review> list = reviewRepository.findByRestNo(new Dinning((int) a));
        System.out.println(list);

        double sum = list.stream().mapToDouble(Review::getRevScore).sum();
        double result = sum /list.size();

        System.out.println(list);


        dinningOptional.ifPresent(dinning -> model.addAttribute("dinning", dinning));

        model.addAttribute("list",list);
        model.addAttribute("reg", result);



        return "detail";
    }









}
