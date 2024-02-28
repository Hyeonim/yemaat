package com.yi.spring.controller;

import com.yi.spring.entity.*;
import com.yi.spring.entity.meta.ImageFrom;
import com.yi.spring.repository.*;
import com.yi.spring.service.DinningService;
import com.yi.spring.service.NoticeService;
import com.yi.spring.service.QAService;
import com.yi.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    UserService userService;

    @Autowired
    DinningService dinningService;

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
    @Autowired
    ImgTableRepository imageTableRepository;

    @Autowired
    ImgTableRepository imgTableRepository;

    @Autowired
    QAService qaService;

    @Autowired
    NoticeService noticeService;


    @GetMapping("/{subPage}")
    public String managerPage(Model model, @PathVariable String subPage) {
        model.addAttribute("page", "managerPage/" + subPage);

        return "managerPage";
    }
//    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ메인ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

//    @GetMapping("/content")
//    public String managerMain(Model model) {
//        List<User> uList = userRepository.findAll();
//        List<Dinning> dList = dinningRepository.findAll();
//        List<QA> qa = qaRepository.findAll();
//
//        HashMap<String, Integer> userStat = new HashMap<>();
//        for (User elem : uList) {
//            String key;
//            if (elem.getUserAuth().equals("3"))
//                continue;
//            if (elem.isUserBlock())
//                key = "4";
//            else
//                key = elem.getUserAuth();
//
//            Integer statCount = userStat.computeIfAbsent(key, k -> 0);
//            statCount++;
//            userStat.put(key, statCount);
//        }
//
//        HashMap<String, Integer> restStat = new HashMap<>();
//        for (Dinning restaurant : dList) {
//            String key = restaurant.getRestCategory();
//            Integer statCount = restStat.computeIfAbsent(key, k -> 0);
//            statCount++;
//            restStat.put(key, statCount);
//        }
//
//        model.addAttribute("drawGraph", true);
//        model.addAttribute("userStat", userStat);
//        model.addAttribute("restStat", restStat);
//        model.addAttribute("uList", uList);
//        model.addAttribute("dList", dList);
//        model.addAttribute("qa", qa);
//        model.addAttribute("page", "managerPage/content");
//
//        return "managerPage";
//    }

    @GetMapping("/content")
    public String managerMain(Model model) {
        List<User> uList = userRepository.findAll();
        List<Dinning> dList = dinningRepository.findAll();
        List<QA> qa = qaRepository.findAll();

        HashMap<String, Integer> userStat = new HashMap<>();
        for (User elem : uList) {
            String key;
            if (elem.getUserAuth().equals("ADMIN"))
                continue;
            if (elem.isUserBlock())
                key = "4";
            else
                key = elem.getUserAuth();

            Integer statCount = userStat.computeIfAbsent(key, k -> 0);
            statCount++;
            userStat.put(key, statCount);
        }

        HashMap<String, Integer> restStat = new HashMap<>();
        for (Dinning restaurant : dList) {
            String key = restaurant.getRestCategory();
            Integer statCount = restStat.computeIfAbsent(key, k -> 0);
            statCount++;
            restStat.put(key, statCount);
        }

        long unansweredCount = qaRepository.countByQaStatusFalse();

        model.addAttribute("drawGraph", true);
        model.addAttribute("userStat", userStat);
        model.addAttribute("restStat", restStat);
        model.addAttribute("uList", uList);
        model.addAttribute("dList", dList);
        model.addAttribute("qa", qa);
        model.addAttribute("header", "관리자 페이지 메인");
        model.addAttribute("unansweredCount", unansweredCount);

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
        model.addAttribute("header", "고객 정보");

        return "managerPage";
    }



//    @GetMapping("/managerPage_UList")
//    public String managerListU(Model model,
//                               @RequestParam(value = "page", defaultValue = "0") int page,
//                               @RequestParam(value = "searchInput", required = false) String searchInput) {
//        Page<User> paging;
//        if (searchInput != null && !searchInput.isEmpty()) {
//            // 검색어가 존재하는 경우
//            paging = userService.findByUserAuthAndUserNameContainingPaged("1", searchInput, page);
//        } else {
//            // 검색어가 없는 경우 전체 목록 조회
//            // 블랙리스트가 아니면서 auth가 1인 회원들만 조회
//            List<User> userList = userService.findByUserAuthAndUserBlockNot("1", true,page);
//            paging = new PageImpl<>(userList, PageRequest.of(page, 10), userList.size());
//        }
//
//        model.addAttribute("users", paging);
//        model.addAttribute("page", "managerPage/managerPage_UList");
//        model.addAttribute("header", "고객 목록");
//
//        return "managerPage";
//    }


    @GetMapping("/managerPage_UList")
    public String managerListU(Model model,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "searchInput", required = false) String searchInput) {
        Page<User> paging;
        if (searchInput != null && !searchInput.isEmpty()) {
            // 검색어가 존재하는 경우
            paging = userService.findByUserAuthAndUserNameContainingPaged("USER", searchInput, page);
        } else {
            // 검색어가 없는 경우 전체 목록 조회
            paging = userService.findByUserNoBlack(page);
        }

        model.addAttribute("users", paging);
        model.addAttribute("page", "managerPage/managerPage_UList");
        model.addAttribute("header", "고객 목록");

        return "managerPage";
    }

    @PostMapping("/UListBlack")
    public String ulackupdateStatus(@RequestParam("userNo") int userNo, @RequestParam("status") String status, RedirectAttributes redirectAttributes) {
        // 가게 번호와 상태를 받아와서 DB에 저장함
        Optional<User> userOptional = userRepository.findByUserNo(userNo);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean isBlocked = status.equals("1"); // "1"은 블랙리스트 설정을 나타내는 값
            user.setUserBlock(isBlocked);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "블랙리스트 상태가 성공적으로 변경되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("error", "유저를 찾을 수 없습니다.");
        }
        return "redirect:/manager/managerPage_UList";
    }











    @GetMapping("/managerPage_UAdd")
    public String managerPageManagerPageUAdd(Model model) {
        model.addAttribute("header", "고객 추가");

        model.addAttribute("page", "managerPage/managerPage_UAdd");

        return "managerPage";
    }

    @GetMapping("/managerPage_UBlackList")
    public String managerBlackListU(Model model, @RequestParam(value = "searchInput", required = false) String searchInput) {

        List<User> users = userRepository.findAll();
        List<User> onlyUsers = new ArrayList<>();

        if (searchInput != null && !searchInput.isEmpty()) {
            users = userRepository.findByUserNameContainingIgnoreCaseAndUserAuthAndUserBlock(searchInput, "USER", true);
        } else {
            users = userRepository.findByUserAuthAndUserBlock("USER", true);
        }

        for (User result : users) {
            if (result.getUserAuth().equals("USER") && result.isUserBlock()) {
                onlyUsers.add(result);
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("users", onlyUsers);
        model.addAttribute("page", "managerPage/managerPage_UBlackList");
        model.addAttribute("header", "블랙리스트 목록");

        return "managerPage";
    }

    @PostMapping("managerPage_UAdd")
    public String managerAddU(@RequestParam MultipartFile file, User user,Model model) {

        System.out.println("11111111111111111111111:"+file);
        System.out.println(user);

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



    @PostMapping("/managerPage_UUpdate")
    public String userUpdate(
            @RequestParam(required = false) MultipartFile file,
            @RequestParam int userNo,
            @RequestParam String userName,
            @RequestParam String userId,
            @RequestParam String userEmail,
            @RequestParam String userPassword,
            @RequestParam String userTel,
            @RequestParam String userAuth,
    @RequestParam boolean userBlock) throws IOException {

        Optional<User> userOptional = userRepository.findByUserNo(userNo);
        userOptional.ifPresent(user -> {
            // 업로드된 파일이 존재하는 경우에만 처리
            if (file != null && !file.isEmpty()) {
                try {
                    byte[] userImg = file.getBytes();
                    user.setUserImg(userImg);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 업로드 중 오류 발생: " + e.getMessage());
                }
            }
            // 사용자 정보 업데이트
            user.setUserName(userName);
            user.setUserId(userId);
            user.setUserEmail(userEmail);
            user.setUserPassword(userPassword);
            user.setUserTel(userTel);
            user.setUserAuth(userAuth);
            user.setUserBlock(userBlock);
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
    public String ManagerQA(Model model
            , @RequestParam(value = "page", defaultValue = "0") int page) {

//        Page<QA> list = qaService.findByUserNoPaged(page);
        Page<QA> list = qaService.findByStatusAsc(page);

        System.out.println(list);

        model.addAttribute("page", "managerPage/managerPage_QA");
        model.addAttribute("header", "문의 목록");

        model.addAttribute("qa", list);
        return "managerPage";

    }

    @GetMapping("managerPage_QAAnswer")
    public String ManagerQAAnswer(@RequestParam int id, Model model) {

        Optional<QA> qa = qaRepository.findById(id);

        model.addAttribute("qa", qa);

        model.addAttribute("page", "managerPage/managerPage_QAAnswer");
        model.addAttribute("header", "문의 답변");

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


//    @GetMapping("/managerPage_Notice")
//    public String managerNoticeList(Model model,
//                                    @RequestParam(value = "page", defaultValue = "0") int page,
//                                    @RequestParam(value = "searchInput4", required = false) String searchInput4) {
//
//        Page<Notice> list = noticeService.findByAll(page);
//        Page<Notice> noticeList;
//
//        if (searchInput4 != null && !searchInput4.isEmpty()) {
//            // 검색어가 존재하는 경우
//            noticeList = noticeService.findBySubjectContaining(searchInput4, page);
//        } else {
//            // 검색어가 없는 경우 전체 목록 조회
//            noticeList = noticeService.findAll(page);
//        }
//
//        model.addAttribute("page", "managerPage/managerPage_Notice");
//        model.addAttribute("list", list);
//
//        return "managerPage";
//    }
    @GetMapping("/managerPage_Notice")
    public String managerNoticeList(Model model,
                                    @RequestParam(value = "page", defaultValue = "0") int page
                                     ) {


List<Notice> head = noticeRepository.findByImportantNotice(true);

        Page<Notice> noticeList = noticeService.findAll(page);


        model.addAttribute("page", "managerPage/managerPage_Notice");
        model.addAttribute("list", noticeList); // 수정된 부분: 검색 결과를 담도록 변경
        model.addAttribute("head", head); // 수정된 부분: 검색 결과를 담도록 변경

        model.addAttribute("header", "공지사항 목록");

        return "managerPage";
    }

    @PostMapping("/managerPage_NoticeHead")
    public String managerNoticeHead(Model model,
                                    @RequestParam() int id,
                                    @RequestParam("head") Boolean importantNotice) {


        System.out.println("id`~~~~~~~~~~~~" + id);


        Optional<Notice> head = noticeRepository.findById(id);

        System.out.println("importantNotice`~~~~~~~~~~~~" + head);



        head.ifPresent(notice -> {
            if (importantNotice) {
                notice.setImportantNotice(true);
                noticeRepository.save(notice);
            } else {
                notice.setImportantNotice(false);
                noticeRepository.save(notice);
            }
        });


//        model.addAttribute("page", "managerPage/managerPage_Notice");

//        model.addAttribute("list", noticeList); // 수정된 부분: 검색 결과를 담도록 변경

//        return "managerPage";
        return "redirect:/manager/managerPage_Notice";

    }

    @GetMapping("/managerPage_NoticeDetail")
    public String managerNoticeDetail(@RequestParam int id,
                                      Model model) {
        Optional<Notice> notice = noticeRepository.findById(id);

        System.out.println(notice);


        model.addAttribute("notice", notice);
        model.addAttribute("page", "managerPage/managerPage_NoticeDetail");
        model.addAttribute("header", "공지사항 상세보기");

        return "managerPage";
    }

    @PostMapping("managerPage_NoticeAdd")
    public String managerNoticeAdd(@RequestParam String subject,
                                   @RequestParam String writer,
                                   @RequestParam String content,
                                   Model model) {

        Notice notice = new Notice();

        notice.setSubject(subject);
        notice.setWriter(writer);
        notice.setContent(content);
        notice.setWriteDate(Instant.now());

        noticeRepository.save(notice);


        return "redirect:/manager/managerPage_Notice";
    }

    @GetMapping("/managerPage_NoticeAdd")
    public String managerPageManagerPageNoticeAdd(Model model) {
        model.addAttribute("header", "공지사항 추가");

        model.addAttribute("page", "managerPage/managerPage_NoticeAdd");

        return "managerPage";
    }


    @PostMapping("managerPage_NoticeUpdate")
    public String noticeUpdate(
            @RequestParam int id,
            @RequestParam String subject,
            @RequestParam String writer,
//            @RequestParam String writeDate,
            Notice notice) throws IOException {

        noticeRepository.save(notice);

        Optional<Notice> noticeOptional = noticeRepository.findById(id);

        return "redirect:/manager/managerPage_Notice";
    }

    @PostMapping("managerPage_NoticeDelete")
    @Transactional
    public String noticeDelete(@RequestParam int id, Model model) {

        noticeRepository.deleteById(id);

        return "redirect:/manager/managerPage_Notice";
    }

//    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ점주꺼ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

//    // 이거 주석된거 지우지 마세요@@@@@@@@@@@@@@@@@@@

//    @GetMapping("/managerPage_JInfo")
//    public String managerInfoA(Model model) {
//
//        model.addAttribute("page", "managerPage/managerPage_JInfo");
//
//        // 사용자 목록 가져오기
//        List<User> userList = userService.getAllUsers();
//
//        System.out.println(userList);
//
//        // 각 사용자가 소유한 가게의 정보를 가져와서 함께 저장
//        List<Dinning> dinningList = new ArrayList<>();
//        for (User user : userList) {
//            if ("2".equals(user.getUserAuth())) {
//                dinningList.addAll(user.getDiningRests()); // 사용자가 소유한 가게들을 가져와서 리스트에 추가하는 거임
//            }
//        }
//
//        System.out.println(dinningList);
//
//        model.addAttribute("dinningList", dinningList); // 가져온 가게 목록 뿌리기
//
//        return "managerPage";
//    }

//    @GetMapping("/managerPage_JList")
//    public String managerInfoA(Model model,
//                               @RequestParam(value = "page", defaultValue = "0") int page,
//                               @RequestParam(value = "searchInput2", required = false) String searchInput2) {
//        // 사용자 목록 가져오기
//        List<User> userList = userService.getAllUsers();
//        //고친거
//        Page<User> paging = this.userService.findByJumNoPaged(page);
//
//        model.addAttribute("users", paging);
////        model.addAttribute("users", users); // 가져온 가게 목록 뿌리기
//        model.addAttribute("page", "managerPage/managerPage_JList");
//
//        return "managerPage";
//    }

    @GetMapping("/managerPage_JList")
    public String managerInfoA(Model model,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "searchInput", required = false) String searchInput) {
        Page<User> paging;
        if (searchInput != null && !searchInput.isEmpty()) {
            // 검색어가 존재하는 경우
            paging = userService.findByUserAuthAndUserNameContainingPaged("OWNER", searchInput, page);
        } else {
            // 검색어가 없는 경우 전체 목록 조회
            paging = userService.findByJumNoPaged(page);
        }

        model.addAttribute("users", paging);
        model.addAttribute("page", "managerPage/managerPage_JList");
        model.addAttribute("header", "점주 목록");

        return "managerPage";
    }


//    @GetMapping("/managerPage_JList")
//    public String managerInfoA(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
//                           @RequestParam(value = "searchInput2", required = false) String searchInput) {
//        Page<Dinning> users;
//
//        if (searchInput != null && !searchInput.isEmpty()) {
//            users = dinningService.searchByDinningNamePaged(page, searchInput);
//        } else {
//            users = dinningService.findByDinningNoPaged(page);
//        }
//
//        model.addAttribute("users", users);
//        model.addAttribute("page", "managerPage/managerPage_JList");
//        return "managerPage";
//    }


    @GetMapping("managerPage_JAdd")
    public String jumAdd(Model model) {

        model.addAttribute("page", "managerPage/managerPage_JAdd");
        model.addAttribute("header", "점주 추가");

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

        return "redirect:/manager/managerPage_JList";
    }

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
        model.addAttribute("header", "점주 상세보기");

        return "managerPage";
    }



    @PostMapping("/managerPage_JUpdate")
    public String jumUpdate(
            @RequestParam(required = false) MultipartFile file,
            @RequestParam int userNo,
            @RequestParam String userName,
            @RequestParam String userId,
            @RequestParam String userEmail,
            @RequestParam String userPassword,
            @RequestParam String userTel,
            @RequestParam String userAuth) throws IOException {

        Optional<User> userOptional = userRepository.findByUserNo(userNo);
        userOptional.ifPresent(user -> {
            // 업로드된 파일이 존재하는 경우에만 처리
            if (file != null && !file.isEmpty()) {
                try {
                    byte[] userImg = file.getBytes();
                    user.setUserImg(userImg);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 업로드 중 오류 발생: " + e.getMessage());
                }
            }
            // 사용자 정보 업데이트
            user.setUserName(userName);
            user.setUserId(userId);
            user.setUserEmail(userEmail);
            user.setUserPassword(userPassword);
            user.setUserTel(userTel);
            user.setUserAuth(userAuth);
            userRepository.save(user);
        });

        return "redirect:/manager/managerPage_JList";
    }

    @PostMapping("managerPage_JDel")
    @Transactional
    public String jumDelete(@RequestParam int userNo, Model model) {

        userRepository.deleteByUserNo(userNo);

        return "redirect:/manager/managerPage_JList";
    }



//    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ가게ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

//앙
    @GetMapping("/managerPage_JrestList")
    public String restInfo(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "searchInput", required = false) String searchInput) {
        Page<Dinning> dinningList;

        if (searchInput != null && !searchInput.isEmpty()) {
            dinningList = dinningService.searchByDinningNameAndStatusPaged(page, searchInput, "NORMAL");
        } else {
            dinningList = dinningService.findByStatusPaged(page, "NORMAL");
        }

        model.addAttribute("dinningList", dinningList);
        model.addAttribute("page", "managerPage/managerPage_JrestList");
        model.addAttribute("header", "가게 목록");

        return "managerPage";
    }

    @GetMapping("/managerPage_JrestWaitList")
    public String restWait(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "searchInput7", required = false) String searchInput7) {
        Page<Dinning> dinningList;

        if (searchInput7 != null && !searchInput7.isEmpty()) {
            dinningList = dinningService.searchByDinningNameAndStatusPaged(page, searchInput7, "WAIT");
        } else {
            dinningList = dinningService.findByStatusPaged(page, "WAIT");
        }

        model.addAttribute("dinningList", dinningList);
        model.addAttribute("page", "managerPage/managerPage_JrestWaitList");
        model.addAttribute("header", "폐점 신청 목록");

        return "managerPage";

    }

    @GetMapping("/managerPage_JrestDetail")
    public String JumRestDetail(Model model, @RequestParam Long restNo) {

        Optional<Dinning> dinningList = dinningRepository.findById(restNo);
        Optional<ImgTb> img = imgTableRepository.findById(Long.valueOf(dinningList.get().getRestImg()));


        model.addAttribute("dinning", dinningList);
        model.addAttribute("img", img);
        model.addAttribute("page", "managerPage/managerPage_JrestDetail");
        model.addAttribute("header", "가게 상세보기");

        return "managerPage";
    }


    @PostMapping("/managerPage_JrestUpdate")
    public String jrestUpdate(
            @RequestParam(required = false) MultipartFile file,
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
            @RequestParam String restDescription) throws IOException {

        Optional<Dinning> dinningList = dinningRepository.findByRestNo(restNo);
        dinningList.ifPresent(din -> {
            // 업로드된 파일이 존재하는 경우에만 처리
            if (file != null && !file.isEmpty()) {
                try {
                    byte[] restImgBytes = file.getBytes();
                    din.setRestImg(din.getRestImgMan().setRestImg(imageTableRepository, ImageFrom.REST, restImgBytes));
                } catch (IOException e) {
                    throw new RuntimeException("이미지 업로드 중 오류 발생: " + e.getMessage());
                }
            }
            // 식당 정보 업데이트
            din.setRestName(restName);
            din.setRestAddr(restAddr);
            din.setRestTel(restTel);
            din.setRestSeat(restSeat);
            din.setRestTime(restTime);
            din.setRestOffDays(restOffDays);
            din.setRestParking(restParking);
            din.setRestMenu(restMenu);
            din.setRestCategory(restCategory);
            din.setRestLatitude(restLatitude);
            din.setRestLongitude(restLongitude);
            din.setRestDescription(restDescription);
            dinningRepository.save(din);
        });

        return "redirect:/manager/managerPage_JrestList";
    }


    @PostMapping("managerPage_JrestAdd")
    public String JrestAdd(@RequestParam(required = false) MultipartFile file, Dinning dinning, Model model) {

        System.out.println(dinning);

        if (file != null && !file.isEmpty()) {
            byte[] restImg = new byte[0];
            try {
                restImg = file.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dinning.setRestImg(dinning.getRestImgMan().setRestImg(imageTableRepository, ImageFrom.REST, restImg));
        }

        dinningRepository.save(dinning);

        return "redirect:/manager/managerPage_JrestList";
    }

    @GetMapping("managerPage_JrestAdd")
    public String JRestPageAdd(Model model) {

        model.addAttribute("page", "managerPage/managerPage_JrestAdd");
        model.addAttribute("header", "가게 추가");

        return "managerPage";
    }

    @PostMapping("managerPage_JrestDel")
    @Transactional
    public String jrestDel(@RequestParam int restNo, Model model) {
        dinningRepository.deleteDinningByRestNo(restNo);

        return "redirect:/manager/managerPage_JrestList";
    }


// 폐점 관련
    @GetMapping("/managerPage_JrestCloseList")
    public String closeRestInfo(Model model,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "searchInput6", required = false) String searchInput6) {

        Page<Dinning> dinningList;

        if (searchInput6 != null && !searchInput6.isEmpty()) {
            dinningList = dinningService.searchByDinningNameAndStatusPaged(page, searchInput6, "CLOSED");
        } else {
            dinningList = dinningService.findByStatusPaged(page, "CLOSED");
        }

        model.addAttribute("dinningList", dinningList);
        model.addAttribute("page", "managerPage/managerPage_JrestCloseList");
        model.addAttribute("header", "폐점 목록");

        return "managerPage";
    }

    @PostMapping("/waitUpd")
    public String updateStatus(@RequestParam("restNo") int restNo, @RequestParam("status") String status, RedirectAttributes redirectAttributes) {
        // 가게 번호와 상태를 받아와서 DB에 저장함
        Optional<Dinning> optionalDinning = dinningRepository.findByRestNo(restNo);
        if (optionalDinning.isPresent()) {
            Dinning dinning = optionalDinning.get();
            dinning.setRestStatus(status);
            dinningRepository.save(dinning);
            redirectAttributes.addFlashAttribute("message", "가게 상태가 성공적으로 변경되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("error", "가게를 찾을 수 없습니다.");
        }
        return "redirect:/manager/managerPage_JrestWaitList";
    }

}