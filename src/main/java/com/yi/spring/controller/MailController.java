package com.yi.spring.controller;

import com.yi.spring.entity.MailVo;
import com.yi.spring.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/CheckMail")
    public String MailPage(){
        return "View";
    }

    @ResponseBody
    @PostMapping("/mail")
    public String MailSend(String mail){

        int number = mailService.sendMail(mail);

        String num = "" + number;

        return num;
    }

}
