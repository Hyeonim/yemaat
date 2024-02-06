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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    // 사용자 페이지 컨트롤러 클래스

    // 유저 컨텐츠 페이지로 이동
    @GetMapping("userPage/{userNo}")
    public String userPageForm(@PathVariable("userNo") int userNo, Model model) {
        model.addAttribute("user", userService.findByUserNo(userNo));
        return "userPage/user_main";
    }

//    @GetMapping("userPage_root/{userNo}")
//    public String GoRoot(@PathVariable("userNo") int userNo, Model model) {
//        model.addAttribute("user", userService.findByUserNo(userNo));
//        return "userPage/user_main";
//    }

    // 유저가 작성한 포스트 목록 페이지로 이동
    @GetMapping("user_posts/{userNo}")
    public String userPosts(@PathVariable("userNo") Long userNo, Model model) {
        List<Reservation> list = reservationRepository.findByUserNo(userNo);
        model.addAttribute("list", list);
        return "userPage/user_posts";
    }

    // 유저가 작성한 리뷰 목록 페이지로 이동
    @GetMapping("user_review/{userNo}")
    public String userReviews(@PathVariable("userNo") User userNo, Model model) {
        List<Review> list = reviewRepository.findByUserNo(userNo);
        model.addAttribute("review", list);
        return "userPage/user_review";
    }

    // 유저가 작성한 Q&A 목록 페이지로 이동
    @GetMapping("user_QA/{userNo}")
    public String userQA(@PathVariable("userNo") User userNo, Model model) {
        List<QA> list = qaRepository.findByUserNo(userNo);
        model.addAttribute("Num", userNo.getUserNo());
        model.addAttribute("QA", list);
        System.out.println(list);
        return "userPage/user_QA";
    }

    // 유저가 Q&A를 추가하는 페이지로 이동
    @GetMapping("user_qa_form/{userNo}")
    public String userQAUpdateForm(@PathVariable("userNo") int userNo, Model model){
        model.addAttribute("QA_userNo",userService.findByUserNo(userNo));
        return "userPage/user_QA_form";
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
        return "redirect:/user/user_content/" + user_no;
    }

    // 유저 정보 페이지로 이동
    @GetMapping("user_info/{userNo}")
    public String userUpdateForm(@PathVariable("userNo") int userNo, Model model) {
        model.addAttribute("user", userService.findByUserNo(userNo));
        return "userPage/user_info";
    }

    // 유저 정보 업데이트를 처리하는 POST 요청 처리
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

    // 유저 목록 페이지로 이동(관리용)
    @GetMapping("list_user")
    public String userList(Model model) {
        List<User> list = userService.getAllUsers();
        model.addAttribute("userList", list);
        return "user/user_list";
    }

    // 유저 추가 페이지로 이동
    @GetMapping("add_user_form")
    public String userAddForm() {
        return "user/user_add_form";
    }

    // 유저 추가를 처리하는 POST 요청 처리
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

    // 유저 삭제를 처리하는 GET 요청 처리
    @Transactional
    @GetMapping("delete_user/{userNo}")
    public String userDelete(@PathVariable("userNo") int userNo) {
        userService.deleteByUserNo(userNo);
        return "redirect:/user/list_user";
    }


}