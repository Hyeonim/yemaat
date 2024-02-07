package com.yi.spring.controller;

import com.yi.spring.entity.QA;
import com.yi.spring.entity.QaAnswer;
import com.yi.spring.entity.User;
import com.yi.spring.repository.QARepository;
import com.yi.spring.repository.QaAnswerRepository;
import com.yi.spring.service.QAService;
import com.yi.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("QA/")
public class QaController {

    @Autowired
    QaAnswerRepository qaAnswerRepository;
    @Autowired
    QARepository qaRepository;
    @Autowired
    QAService qaService;
    @Autowired
    UserService userService;

    // 유저가 작성한 Q&A 목록 페이지로 이동
    @GetMapping("user_qa/{userNo}")
    public String userQA(@PathVariable("userNo") User userNo, @RequestParam(value = "page", defaultValue = "0") int page , Model model) {
        Page<QA> paging = this.qaService.findByUserNoPaged(userNo, page);
        int userNoCount = qaService.countByUserNo(userNo);
//        Page<QA> paging = this.qaService.findByUserNo(userNo.getUserNo())
//        model.addAttribute("userNoCount", userNoCount);
        model.addAttribute("Num", userNo.getUserNo());
        model.addAttribute("QA", paging);

        System.out.println(userNoCount);
        System.out.println("--------" + paging.toString() + " ---- " + paging.getSize());
        return "userPage/user_QA";
    }

    // 유저가 Q&A를 추가하는 페이지로 이동
    @GetMapping("user_qa_form/{userNo}")
    public String userQAUpdateForm(@PathVariable("userNo") int userNo, Model model){
        model.addAttribute("QA_userNo",userService.findByUserNo(userNo));
        return "userPage/user_QA_form";
    }

    @GetMapping("Qa_answer/{qaNo}")
    public String QaAnswer(@PathVariable("qaNo") int qaNo, Model model) {


        model.addAttribute("QA", qaRepository.findById(qaNo));
        Optional<QaAnswer> qaAnswerOptional = qaAnswerRepository.findByQaNo(qaNo);
        model.addAttribute("QaAnswer", qaAnswerOptional.orElse(null));


        System.out.println("문의 내용 --> " + qaRepository.findById(qaNo));
        System.out.println("답변 내용 --> " + qaAnswerRepository.findByQaNo(qaNo));
        return "userPage/user_QA_answer_form";
    }

    // 유저가 Q&A를 추가하는 POST 요청 처리
    @PostMapping("user_qa_add")
    public String userQAUpdate(@RequestParam("userNo") User userNo,
                               @RequestParam("qa_title") String qaTitle,
                               @RequestParam("qa_content") String qaContent) {
        QA qa = new QA();
        qa.setUserNo(userNo);
        qa.setQaTitle(qaTitle);
        qa.setQaContent(qaContent);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        qa.setQaWriteTime(LocalDateTime.parse(formattedDateTime, formatter));
        qaRepository.save(qa);

        int user_no = userNo.getUserNo();
        return "redirect:/user/userPage/" + user_no;
    }

}
