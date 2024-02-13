package com.yi.spring.controller;

import com.yi.spring.entity.*;
import com.yi.spring.repository.*;
import com.yi.spring.service.QAService;
import com.yi.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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
    @Autowired
    QAService qaService;
    @Autowired
    DinningRepository dinningRepository;


    public List<Dinning> getRestaurantsForLatestReservation(Long userNo){
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("resTime")));
        List<Reservation> latestReservationList = reservationRepository.findLatestReservationByUserNo(userNo, pageRequest);

        if (!latestReservationList.isEmpty()) {
            Long latestRestNo = latestReservationList.get(0).getRestNo();
            return dinningRepository.findByRestNo(latestRestNo);
        } else {
            // 예약 기록이 없는 경우 처리
            return Collections.emptyList();
        }
    }

    // 유저 컨텐츠 페이지로 이동
    @GetMapping("userPage/{userNo}")
    public String userPageForm(@PathVariable("userNo") int userNo, Model model) {

        List<Dinning> restaurantsForLatestReservation = getRestaurantsForLatestReservation((long) userNo);

        model.addAttribute("user", userService.findByUserNo(userNo));
        model.addAttribute("restaurants", restaurantsForLatestReservation);

        System.out.println(restaurantsForLatestReservation);

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
        int user_no = userNo;
        return "redirect:/user/userPage/" + user_no;
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