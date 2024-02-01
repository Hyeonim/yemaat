package com.yi.spring.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    @GetMapping("/{subPage}")
    public String handleRequest(Model model, @PathVariable String subPage) {
//        model.addAttribute( "page", subPage );
        model.addAttribute( "page", "mypage/"+subPage );
//        model.addAttribute( "page2", "/templates/mypage/myrest::content" );

        return "mypage/mypage";
    }
}
