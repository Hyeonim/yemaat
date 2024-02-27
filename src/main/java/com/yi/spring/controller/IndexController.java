package com.yi.spring.controller;

import com.yi.spring.entity.*;
import com.yi.spring.entity.meta.DinningReviewView;
import com.yi.spring.repository.*;
import com.yi.spring.service.EventService;
import com.yi.spring.service.NoticeService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    DinningRepository dinningRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImgTableRepository imageTableRepository;

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    NoticeService noticeService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventService eventService;

    @Autowired
    DinningWithReviewRepository dinningWithReviewRepository;

    @GetMapping("/")
    public String home() {

        return "redirect:/home";
    }
    @GetMapping("/home")
    public String index(Model model, HttpSession httpSession, HttpServletRequest request) {
        List<Event> eventList = eventService.getNewEvents(); // 상단 배너 새로운 이벤트
        model.addAttribute("eventList", eventList);

        List<Notice> NList = noticeRepository.getList(); // 상단 배너 공지사항 목록
        List<Notice> head = noticeRepository.findByImportantNotice(true); //고정 공지

        model.addAttribute("NList", NList);
        model.addAttribute("head", head);


        List<Dinning> dList = dinningRepository.getRandomList("1"); // 상단 배너 추천 식당(랜덤)
        if (dList != null && !dList.isEmpty()) {
            /*
            Random random = new Random();
            int randomIndex = random.nextInt(dList.size()); // 리스트의 크기 내에서 랜덤 인덱스 생성
            Dinning randomData = dList.get(randomIndex); // 랜덤한 요소 선택
             */
            model.addAttribute("randomData", dList.get(0)); // 모델에 랜덤한 요소 추가
        }



//        List<Review> reviewList = new ArrayList<>();
//        for ( int i = 0; i < 2; i++ )
//            reviewList.addAll( reviewRepository.getRandomList( 10 ) );
//
//        List<Object[]> test = reviewRepository.getImgTest();
//        List<Object[]> test2 = reviewRepository.getImgTest2();
//        List<Tuple> test3 = reviewRepository.getImgTest3();

        List<DinningReviewView> dinningReviewList1 = RestSearchController.searchMain( null, Map.of("filter1","3"), 12 , dinningWithReviewRepository);
        List<DinningReviewView> dinningReviewList2 = RestSearchController.searchMain( null, Map.of("filter1","2"), 12 , dinningWithReviewRepository);
        List<DinningReviewView> dinningReviewList3 = RestSearchController.searchMain( null, Map.of("filter1","1"), 12 , dinningWithReviewRepository);

        model.addAttribute("dinningReviewList1", dinningReviewList1 );
        model.addAttribute("dinningReviewList2", dinningReviewList2 );
        model.addAttribute("dinningReviewList3", dinningReviewList3 );






        return "main";
    }

    @GetMapping("/homeSlide")
    public String homeSlide(Model model){
        List<Review> reviewList = reviewRepository.getRandomList( 10 );
        model.addAttribute("list", reviewList );
        model.addAttribute("bSlide", 1 );
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



    @GetMapping("/restHost")
    public String host(Model model, HttpSession httpSession) {
        return "restHost";
    }

    @GetMapping("addRest")
    public String addRest() {
        return "myPage/addRest";
    }




    @GetMapping("/api/loadImg/{imgNo}")
    public ResponseEntity<String> getImage(@PathVariable String imgNo){
        ImgTb imgDb = imageTableRepository.findById(Long.valueOf(imgNo)).orElse(null);
        String result = "";
        if ( null != imgDb && null != imgDb.getBytes())
            result = Base64.getEncoder().encodeToString(imgDb.getBytes());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/api/getChatRoomId")
    public ResponseEntity<String> getCustomerChatRoom( HttpServletRequest request, Principal principal )
    {
        String userId = null;
        if ( null != principal )
            userId = principal.getName();

        String chatRoomId = "";
        if ( null != userId ) {
            ServletContext servletContext = request.getServletContext();
            chatRoomId = (String) servletContext.getAttribute("user" + userId);
            if (null == chatRoomId) {
                chatRoomId = UUID.randomUUID().toString().replace("-", "");
                servletContext.setAttribute("user" + userId, chatRoomId);
            }
        }
        return new ResponseEntity<>( chatRoomId, HttpStatus.OK );
    }



    @GetMapping("chatTest")
    public String chatTest(){
        return "/chat/chatroomTest";
    }

}