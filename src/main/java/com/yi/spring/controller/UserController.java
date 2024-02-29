package com.yi.spring.controller;

import com.yi.spring.OAuth2.OAuth2MemberService;
import com.yi.spring.entity.*;
import com.yi.spring.repository.*;
import com.yi.spring.service.QAService;
import com.yi.spring.service.ReservationService;
import com.yi.spring.service.ReviewService;
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
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    @Autowired
    ReservationService reservationService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    private OAuth2MemberService o2MemberService;


    private User user = null;

    public List<Dinning> getRestaurantsForLatestReservation(Long userNo) {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("resTime")));
        List<Reservation> latestReservationList = reservationRepository.findLatestReservationByUserNo(userNo, pageRequest);

        if (!latestReservationList.isEmpty()) {
            List<Dinning> dinningList = new ArrayList<>();
            for (Reservation r : latestReservationList)
                dinningList.add(r.getRestNo());
            return dinningList;
//            Long latestRestNo = latestReservationList.get(0).getRestNo();
//            return dinningRepository.findByRestNo(latestRestNo);
        } else {
            // 예약 기록이 없는 경우 처리
            return Collections.emptyList();
        }
    }

    // 유저 컨텐츠 페이지로 이동
    @GetMapping("userPage")
    public String userPageForm1(Principal principal, Model model) {
//        user = userRepository.findByUserId(principal.getName()).get();
        user = o2MemberService.findUser( principal );

        List<Dinning> restaurantsForLatestReservation = getRestaurantsForLatestReservation(Long.valueOf(user.getUserNo()));

        model.addAttribute("user", user);
        model.addAttribute("restaurants", restaurantsForLatestReservation);

        System.out.println(user);
        return "userPage/user_main";
    }

    // 유저가 작성한 포스트 목록 페이지로 이동
    @GetMapping("user_posts")
    public String userPosts(Principal principal, Model model
                            ,@RequestParam(value = "searchInput", required = false) String searchInput
                            ,@RequestParam(value = "filterExpired", defaultValue = "") String filterExpired
                            ,@RequestParam(value = "filterReview", defaultValue = "") String filterReview
                            ,@RequestParam(value = "filterWait", defaultValue = "") String filterWait
                            ,@RequestParam(value = "filterResCompleted", defaultValue = "") String filterResCompleted
                            ,@RequestParam(value = "filterAll", defaultValue = "") String filterAll){
//        User user = userRepository.findByUserId(principal.getName()).orElse(null);
        user = o2MemberService.findUser( principal );
        reservationRepository.updateReservationStatusToReviewWithJoin2();
        Long userNo = Long.valueOf(user.getUserNo());
        List<Reservation> reservations = reservationRepository.findReservationDetailsByUserNo(userNo);

        System.out.println("searchInput => " + searchInput);

        if (user != null || filterAll.equals("All")) {

            if (searchInput != null && !searchInput.isEmpty()) {
                List<Reservation> reservationsList = reservationRepository.findReservationDetailsByUserNoAndRestName(userNo, searchInput);
                model.addAttribute("list", reservationsList);
                reservationService.processReservations(reservations);
                reservationService.checkReservationStatus(reservations, model);
            } else {
//                List<Reservation> reservations = reservationRepository.findReservationDetailsByUserNo(userNo);
                model.addAttribute("list", reservations);
                reservationService.processReservations(reservations);
                reservationService.checkReservationStatus(reservations, model);
            }

//            reservationService.processReservations(reservations);
//            reservationService.checkReservationStatus(reservations, model);
//            System.out.println("res => " + reservations);


            if (filterExpired.equals("expired")){
                List<Reservation> reservationsExpired = reservationRepository.ReservationStatusEXPIRED(userNo);
                reservationService.checkReservationStatus(reservations, model);

                model.addAttribute("list", reservationsExpired);
            }

            if (filterReview.equals("review")){
                List<Reservation> reservationsReview = reservationRepository.ReservationStatusREVIEW(userNo);
                reservationService.checkReservationStatus(reservations, model);

                model.addAttribute("list", reservationsReview);
            }

            if (filterWait.equals("wait")){
                List<Reservation> reservationsWait = reservationRepository.ReservationStatusWAIT(userNo);
                reservationService.checkReservationStatus(reservations, model);

                model.addAttribute("list", reservationsWait);
            }

            if (filterResCompleted.equals("res_completed")){
                List<Reservation> reservationsResCompleted = reservationRepository.ReservationStatusRESERVE_COMPLETED(userNo);
                reservationService.checkReservationStatus(reservations, model);

                model.addAttribute("list", reservationsResCompleted);
            }
        }

        model.addAttribute("main_user", user);
        return "userPage/user_posts";
    }

    @PostMapping("submitReview")
    public String reviewAdd(Principal principal,Review review,@RequestParam MultipartFile file){
//        user = userRepository.findByUserId(principal.getName()).get();
        user = o2MemberService.findUser( principal );

        byte[] revImg;
        try {
            revImg = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        review.setRevScore((int) (review.getRevScore() * 10));
        review.setUserNo(user);
        review.setRevImg(revImg);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        review.setRevWriteTime(LocalDateTime.parse(formattedDateTime, formatter));
        review.setRevStatus(String.valueOf(ReviewStatus.NORMAL));

        reviewRepository.save(review);
        reservationRepository.updateReservationStatusToReviewWithJoin();
        return "redirect:/user/user_posts";
    }

    // 유저가 작성한 리뷰 목록 페이지로 이동
    @GetMapping("user_review")
    public String userReviews(Principal principal,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "searchInput", required = false) String searchInput ,Model model) {
        user = o2MemberService.findUser( principal );
//        System.out.println("search => " + searchInput);

        if (user != null) {
            List<Dinning> restaurantsForLatestReservation = getRestaurantsForLatestReservation(Long.valueOf(user.getUserNo()));
            List<Reservation> reservations = reservationRepository.findReservationDetailsByUserNo(Long.valueOf(user.getUserNo()));
            Page<Review> reviewsPage;
            List<Review> reviewsList;

            if (searchInput != null && !searchInput.isEmpty()) {
                reviewsPage = reviewService.findByRevContentContainingPaged(user, searchInput, page);
                reviewsList = reviewsPage.getContent();
                model.addAttribute("userNoCount", reviewsList.size());
//                System.out.println("reviewsPage있다 => " + reviewsPage.getContent());

                if (reviewsList.isEmpty()) {
                    System.out.println("검색 결과가 없습니다.");
                    model.addAttribute("noResultsMessage", "검색 결과가 없습니다.");
                }
            } else {
                reviewsPage = reviewService.findByUserNoOrderByRevWriteTimeDesc(user, page);
                reviewsList = reviewsPage.getContent();
                long userNoCount = reviewRepository.countByUserNo(user);
                model.addAttribute("userNoCount", userNoCount);
//                System.out.println("reviewsPage없다 => " + reviewsPage.getContent());
            }

            reservationService.checkReservationStatus(reservations, model);

            model.addAttribute("main_user", user);
            model.addAttribute("restaurants", restaurantsForLatestReservation);
            model.addAttribute("list", reservations);


            List<String> timeAgoList = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            LocalDateTime currentTime = LocalDateTime.now();

            for (Review review : reviewsList) {
                if (review.getRevWriteTime() != null) {
                    LocalDateTime reviewTime = review.getRevWriteTime();
                    long minutesAgo = ChronoUnit.MINUTES.between(reviewTime, currentTime);

                    if (minutesAgo < 60) {
                        timeAgoList.add((minutesAgo == 0) ? "방금전" : minutesAgo + "분전");
                    } else {
                        long hoursAgo = ChronoUnit.HOURS.between(reviewTime, currentTime);

                        if (hoursAgo < 24) {
                            timeAgoList.add(hoursAgo + "시간전");
                        } else {
                            long daysAgo = ChronoUnit.DAYS.between(reviewTime, currentTime);

                            if (daysAgo < 7) {
                                timeAgoList.add(daysAgo + "일전");
                            } else {
                                long weeksAgo = daysAgo / 7;

                                if (weeksAgo < 5) {
                                    timeAgoList.add(weeksAgo + "주전");
                                } else {
                                    long monthsAgo = daysAgo / 30;

                                    if (monthsAgo < 12) {
                                        timeAgoList.add(monthsAgo + "개월전");
                                    } else {
                                        long yearsAgo = monthsAgo / 12;
                                        timeAgoList.add(yearsAgo + "년전");
                                    }
                                }
                            }
                        }
                    }
                } else {
                    timeAgoList.add("작성 시간 없음");
                }
            }

            List<Map<String, Object>> combinedList = new ArrayList<>();
            for (int i = 0; i < reviewsList.size(); i++) {
                Map<String, Object> combinedItem = new HashMap<>();
                combinedItem.put("rev", reviewsList.get(i));
                combinedItem.put("timeAgo", timeAgoList.get(i));
                combinedList.add(combinedItem);
            }

            // 리뷰 + 작성시간 데이터
            model.addAttribute("combinedList", combinedList);
            // 페이징 용
            model.addAttribute("reviewsPage", reviewsPage);



        }

        return "userPage/user_review";
    }
    @GetMapping("deleteReview/{revNo}")
    public String reviewDelete(@PathVariable("revNo") int revNo) {
        reviewRepository.deleteById(revNo);
        reservationRepository.updateReservationStatusToReviewWithJoin2();
        return "redirect:/user/user_review";
    }
    @GetMapping("updateReviewForm/{revNo}")
    public String reviewUpdateForm(Principal principal,@PathVariable("revNo") int revNo, Model model){
        user = o2MemberService.findUser( principal );
        Long userNo = Long.valueOf(user.getUserNo());

        List<Dinning> restaurantsForLatestReservation = getRestaurantsForLatestReservation(Long.valueOf(user.getUserNo()));
        List<Reservation> reservations = reservationRepository.findReservationDetailsByUserNo(userNo);
        reservationService.checkReservationStatus(reservations, model);

        model.addAttribute("review", reviewRepository.findById(revNo));
        model.addAttribute("main_user", user);
        model.addAttribute("restaurants", restaurantsForLatestReservation);

        return "userPage/user_reviewUpdate";
    }
    @PostMapping("updateReview/{revNo}")
    public String reviewUpdate(Principal principal,Review review,@RequestParam MultipartFile file,@PathVariable("revNo") int revNo){
        user = o2MemberService.findUser( principal );

        System.out.println("file ->" + file);
        Review existReview = reviewRepository.findById( revNo ).orElse( null );

        if (file != null && !file.isEmpty()) {
            try {
                byte[] revImg = file.getBytes();
                review.setRevImg(revImg);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류 발생: " + e.getMessage());
            }
        } else
        {
            review.setRevImg( existReview.getRevImg());
        }
        review.setRevScore((int) (review.getRevScore() * 10));
        review.setRevStatus(String.valueOf(ReviewStatus.NORMAL));

        review.setId(revNo);
        reviewRepository.save(review);
//        reservationRepository.updateReservationStatusToReviewWithJoin();
        return "redirect:/user/user_review";
    }


    // 유저 정보 페이지로 이동
    @GetMapping("user_info")
    public String userUpdateForm(Principal principal, Model model) {
//        user = userRepository.findByUserId(principal.getName()).get();
        user = o2MemberService.findUser( principal );

        List<Dinning> restaurantsForLatestReservation = getRestaurantsForLatestReservation(Long.valueOf(user.getUserNo()));

        Long userNo = Long.valueOf(user.getUserNo());
        List<Reservation> reservations = reservationRepository.findReservationDetailsByUserNo(userNo);
        reservationService.checkReservationStatus(reservations, model);

        model.addAttribute("main_user", user);
        model.addAttribute("restaurants", restaurantsForLatestReservation);
        model.addAttribute("user", userService.findByUserNo(user.getUserNo()));
        return "userPage/user_info";
    }
    // 유저 정보 업데이트를 처리하는 POST 요청 처리
    @PostMapping("user_update")
    public String userUpdate(Principal principal, @RequestParam MultipartFile file,
                             @RequestParam String userName,
                             @RequestParam String userId,
                             @RequestParam String userEmail,
                             @RequestParam String userPassword,
                             @RequestParam String userTel
    ) throws IOException {
        user = o2MemberService.findUser(principal);
        User existUser = userService.findByUserNo(user.getUserNo()).orElse(null);

        if (file != null && !file.isEmpty()) {
            try {
                byte[] revImg = file.getBytes();
                user.setUserImg(revImg);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류 발생: " + e.getMessage());
            }
        } else
        {
            user.setUserImg( existUser.getUserImg());
        }

        user.setUserName(userName);
        user.setUserId(userId);
        user.setUserEmail(userEmail);
        user.setUserPassword(userPassword);
        user.setUserTel(userTel);

        userRepository.save(user);
        return "redirect:/user/user_info";
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