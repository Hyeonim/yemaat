package com.yi.spring.controller;

import com.yi.spring.entity.*;
import com.yi.spring.repository.*;
import com.yi.spring.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    UserService userService;


    @Autowired
    UserRepository userRepository;

    @Autowired
    QARepository qaRepository;

    @Autowired
    QaAnswerRepository qaAnswerRepository;

    @Autowired
    DinningRepository dinningRepository;

    @Autowired
    NoticeRepository noticeRepository;

    @GetMapping("/{subPage}")
    public String managerPage(Model model, @PathVariable String subPage) {
        model.addAttribute("page", "managerPage/" + subPage);

        return "managerPage";
    }
//    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ메인ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @GetMapping("/content")
    public String managerDetailU(Model model) {

        List<User> uList = userRepository.findAll();
        List<Dinning> dList = dinningRepository.findAll();

        model.addAttribute("uList", uList);
        model.addAttribute("dList", dList);

        model.addAttribute("page", "managerPage/content");

        return "managerPage";
    }


//    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ유저꺼ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @GetMapping("/managerPage_UDetail")
    public String managerDetailU(Model model, @RequestParam int userNo) {


        Optional<User> user = userRepository.findByUserNo(userNo);
//        System.out.println(user);

        model.addAttribute("user", user);
        model.addAttribute("page", "managerPage/managerPage_UDetail");

        return "managerPage";
    }


    @GetMapping("/managerPage_UList")
    public String managerListU(Model model) {

        List<User> users = userRepository.findAll();
        List<User> onlyUsers = new ArrayList<>();
        for (User result : users) {
            if (result.getUserAuth().equals("1")) {
                onlyUsers.add(result);
            }
        }
        model.addAttribute("users", onlyUsers);


        model.addAttribute("page", "managerPage/managerPage_UList");
        return "managerPage";
    }

    @GetMapping("/managerPage_UBlackList")
    public String managerBlackListU(Model model) {

        List<User> users = userRepository.findAll();

        List<User> onlyUsers = new ArrayList<>();

        for (User result : users) {
            if (result.getUserAuth().equals("1") && result.isUserBlock()) {


                onlyUsers.add(result);
            }


        }

//        System.out.println(onlyUsers);

        model.addAttribute("users", onlyUsers);

        model.addAttribute("page", "managerPage/managerPage_UBlackList");
        return "managerPage";
    }


    @PostMapping("managerPage_UAdd")
    public String managerAddU(@RequestParam MultipartFile file, User user, Model model) {


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

        return "redirect:/manager/managerPage_UList";
    }


    @PostMapping("managerPage_UUpdate")
    public String userUpdate(
            @RequestParam MultipartFile file,
            @RequestParam int userNo,
            @RequestParam String userName,
            @RequestParam String userId,
            @RequestParam String userEmail,
            @RequestParam String userPassword,
            @RequestParam String userTel,
            @RequestParam String userAuth,
            User users) throws IOException {

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
        return "redirect:/manager/managerPage_UList";
    }


    @PostMapping("managerPage_UDel")
    @Transactional
    public String managerDelU(@RequestParam int userNo, Model model) {

        userRepository.deleteByUserNo(userNo);


        return "redirect:/manager/managerPage_UList";
    }


    @GetMapping("/managerPage_UBlack")
    public String toggleUserBlock(@RequestParam int userNo, @RequestParam("confirm") boolean confirm) {

        System.out.println("번호~~~~~~~~~~~~~~~~~" + userNo);
        System.out.println("선택~~~~~~~~~~~~~~~~~" + confirm);

        Optional<User> userOptional = userRepository.findByUserNo(userNo);

        userOptional.ifPresent(user -> {
            if (confirm) {
                user.setUserBlock(false);
                userRepository.save(user);
            } else {
                user.setUserBlock(true);
                userRepository.save(user);
            }
        });

        return "redirect:/manager/managerPage_UBlackList";
    }


//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ문의ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @GetMapping("managerPage_QA")
    public String ManagerQA(Model model) {

        List<QA> list = qaRepository.findAll();

        System.out.println(list);

        model.addAttribute("page", "managerPage/managerPage_QA");

        model.addAttribute("qa", list);
        return "managerPage";

    }

    @GetMapping("managerPage_QAAnswer")
    public String ManagerQAAnswer(@RequestParam int id, Model model) {

        Optional<QA> qa = qaRepository.findById(id);

        model.addAttribute("qa", qa);

        model.addAttribute("page", "managerPage/managerPage_QAAnswer");

        return "managerPage";

    }

    @PostMapping("managerPage_QARequest")
    public String ManagerQARequest(@RequestParam int id,
                                   @RequestParam String title,
                                   @RequestParam String content,
                                   QaAnswer qaAnswer,
                                   Model model) {

        Optional<QA> guestQA = qaRepository.findById(id);

        qaAnswer.setAnswerTitle(title);
        qaAnswer.setAnswerContent(content);
        qaAnswer.setQaNo(id);

        qaAnswerRepository.save(qaAnswer);

        guestQA.ifPresent(qa -> {
            if (qaAnswer.getQaNo() == guestQA.get().getId()) {
                qa.setQaStatus(true);
                qaRepository.save(qa);
            }
        });

        return "redirect:/manager/managerPage_QA";
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ공지사항ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


    @GetMapping("/managerPage_Notice")
    public String managerNoticeList(Model model) {

        List<Notice> list = noticeRepository.findAll();

//        System.out.println(list);


        model.addAttribute("page", "managerPage/managerPage_Notice");
        model.addAttribute("list", list);

        return "managerPage";
    }

    @GetMapping("/managerPage_NoticeDetail")
    public String managerNoticeDetail(@RequestParam int id,
                                      Model model) {
        Optional<Notice> notice = noticeRepository.findById(id);

        System.out.println(notice);


        model.addAttribute("notice", notice);
        model.addAttribute("page", "managerPage/managerPage_NoticeDetail");

        return "managerPage";
    }


//    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ점주꺼ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @GetMapping("/managerPage_JInfo")
    public String managerInfoA(Model model) {

//        model.addAttribute("page", "managerPage/managerPage_JInfo");
//
//        List<User> userList = userService.getAllUsers();
//
//        List<User> Owner = new ArrayList<>();
//        for (User result : userList) {
//            if (result.getUserAuth().equals("2")) {
//                Owner.add(result);
//            }
//        }
//        model.addAttribute("userList", Owner);
//
//
//        return "managerPage";

        model.addAttribute("page", "managerPage/managerPage_JInfo");

        // 사용자 목록 가져오기
        List<User> userList = userService.getAllUsers();

        // 각 사용자가 소유한 가게의 정보를 가져와서 함께 저장
        List<Dinning> dinningList = new ArrayList<>();
        for (User user : userList) {
            if ("2".equals(user.getUserAuth())) {
                dinningList.addAll(user.getDiningRests()); // 사용자가 소유한 가게들을 가져와서 리스트에 추가하는 거임
            }
        }

        model.addAttribute("dinningList", dinningList); // 가져온 가게 목록 뿌리기

        return "managerPage";
    }


    @PostMapping("managerPage_JAdd")
    public String jumAdd(@RequestParam MultipartFile file, User user, Model model) {

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

        return "redirect:/manager/managerPage_JInfo";
    }


//    @GetMapping("managerPage_JDetail")
//    public String JumDetail(@RequestParam final int userNo, Model model){
//        userRepository.findByUserNo(userNo);
//        model.addAttribute("JumDetail", userNo);
//
//        return "managerPage_JDetail";
//    }

    @GetMapping("/managerPage_JDetail")
    public String JumDetail(Model model, @RequestParam int userNo) {
        // 해당 userNo에 해당하는 사용자 정보 가져오기
        Optional<User> userOptional = userRepository.findByUserNo(userNo);
        User user = userOptional.orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자가 소유한 가게들 가져오기
        List<Dinning> dinningList = new ArrayList<>();
        if ("2".equals(user.getUserAuth())) {
            dinningList.addAll(user.getDiningRests());
        }

        model.addAttribute("user", user);
        model.addAttribute("dinningList", dinningList); // 가게 목록도 모델에 추가

        model.addAttribute("page", "managerPage/managerPage_JDetail");

        return "managerPage";
    }

//    @PostMapping("/managerPage_JDetail")
//    public String


    @PostMapping("managerPage_JUpdate")
    public String jumUpdate(
            @RequestParam MultipartFile file,
            @RequestParam int userNo,
            @RequestParam String userName,
            @RequestParam String userId,
            @RequestParam String userEmail,
            @RequestParam String userPassword,
            @RequestParam String userTel,
            @RequestParam String userAuth,
            User users) throws IOException {

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

        return "redirect:/manager/managerPage_JInfo?";
    }


    @PostMapping("managerPage_JDel")
    @Transactional
    public String jumDelete(@RequestParam int userNo, Model model) {


        System.out.println("123123123123123123123:" + userNo);

        userRepository.deleteByUserNo(userNo);


        return "redirect:/manager/managerPage_JInfo";
    }

//    @PostMapping("managerPage_JStoreDel")
//    @Transactional
//    public String jumStoreDelete( @RequestParam int userNo, @RequestParam int restNo, Model model) {
//
//
//        System.out.println("12312312312312312userNo:" + userNo);
//        System.out.println("123123123123123123123:" + restNo);
//
//        userRepository.deleteByUserNo(userNo);
////        dinningRepository.deleteDinningByRestNo(restNo);
//
//        return "redirect:/manager/managerPage_JInfo";
//    }

    @GetMapping("managerPage_JrestInfo")
    public String restInfo(Model model) {

        List<Dinning> dinningList = dinningRepository.findAll();

        model.addAttribute("dinningList", dinningList);
        model.addAttribute("page", "managerPage/managerPage_JrestInfo");

        return "managerPage";
    }

    @GetMapping("/managerPage_JrestDetail")
    public String JumRestDetail(Model model, @RequestParam int restNo) {

//        System.out.println("번호~~~~~~~~~~~~~~~~~~~~~~~:" +restNo);

        Optional<Dinning> dinningList = dinningRepository.findByRestNo(restNo);
//        System.out.println(dinningList);

        model.addAttribute("dinning", dinningList);
        model.addAttribute("page", "managerPage/managerPage_JrestDetail");

        return "managerPage";
    }

    @PostMapping("/managerPage_JrestUpdate")
    public String jumrestUpdate(
            @RequestParam MultipartFile file,
            @RequestParam int restNo,
            @RequestParam String restName,
            @RequestParam String restAddr,
            @RequestParam String restTel,
            @RequestParam String restSeat,
            @RequestParam String restTime,
            @RequestParam String restOffDays,
            @RequestParam String restParking,
            @RequestParam String restMenu,
            @RequestParam String restCategory,
            @RequestParam Double restLatitude,
            @RequestParam Double restLongitude,
            @RequestParam String restDescription,
            Dinning dinning) throws IOException {


//        System.out.println("hhhhhhhhhhhhh" + restNo);

        dinningRepository.save(dinning);

        Optional<Dinning> dinningList = dinningRepository.findByRestNo(restNo);
        dinningList.ifPresent(din -> {
            try {
                byte[] restImgBytes = file.getBytes();
                din.setRestImg(restImgBytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dinningRepository.save(din);
        });

        return "redirect:/manager/managerPage_JrestInfo";
    }


}


