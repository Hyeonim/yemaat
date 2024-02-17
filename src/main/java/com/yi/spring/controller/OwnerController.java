package com.yi.spring.controller;

import com.yi.spring.entity.*;
import com.yi.spring.repository.DeleteUserRepository;
import com.yi.spring.repository.ReservationRepository;
import com.yi.spring.service.DiningRestService;
import com.yi.spring.service.EventService;
import com.yi.spring.service.MenuService;
import com.yi.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/owner/*")
public class OwnerController {
    @Autowired
    private UserService userService;
    @Autowired
    private DiningRestService diningRestService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EventService eventService;

    @Autowired
    private DeleteUserRepository deleteUserRepository;
//    @Autowired
//    private ModelMapper modelMapper;

    private List<Reservation> status(List<Reservation> list) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        for (Reservation reservation : list) {
            Timestamp resTimestamp = Timestamp.valueOf(reservation.getResTime());

            if (ReservationStatus.RESERVE_COMPLETED.name().equals(reservation.getRes_status())) {
                if (currentDateTime.isAfter(resTimestamp.toLocalDateTime())) {
                    reservation.setRes_status(ReservationStatus.EXPIRED.name());
                    reservationRepository.save(reservation);
                }
            } else if (ReservationStatus.WAIT.name().equals(reservation.getRes_status())) {
                if (currentDateTime.isAfter(resTimestamp.toLocalDateTime())) {
                    reservation.setRes_status(ReservationStatus.REST_CANCEL.name());
                    reservationRepository.save(reservation);
                }
            }
        }
        return list;
    }

    @GetMapping("home")
    public String home(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        model.addAttribute("user", loginUser);

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        model.addAttribute("dinning", dinning);

        model.addAttribute("pageName", "DASH BOARD");
        return "owner/home";
    }

    @GetMapping("addRest")
    public String addRest(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        model.addAttribute("user", loginUser);
        model.addAttribute("pageName", "식당 등록");
        return "owner/addRest";
    }

    @PostMapping("addRest") // 등록 화면으로 전환은 되는데 등록은 안됨
    public String addRest(Principal principal, Dinning dinning, Model model, @RequestParam MultipartFile file) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        byte[] restImg;
        try {
            restImg = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dinning.setRestImg(restImg);
        dinning.setUserNo(loginUser);
        dinning.setRestStatus(String.valueOf(DinningStatus.NORMAL));
        diningRestService.createRestaurant(dinning);
        return "redirect:/owner/home";
    }

    // -------------------------- 가게 상세보기 및 메뉴 관리 -----------------------------------------
    @GetMapping("viewRest")
    public String viewRest(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        model.addAttribute("user", loginUser);

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        System.out.println(dinning);

        if(dinning == null) {
            model.addAttribute("msg", "등록된 가게가 없습니다. 이 기능은 가게 등록 후 사용 가능한 메뉴입니다.");
            model.addAttribute("location", "/owner/addRest");
            return "redirect:/owner/addRest";
        }
        model.addAttribute("dinning", dinning);


        List<Menu> menuList = menuService.getMenusByRestNo(dinning.getRestNo());
        model.addAttribute("menuList", menuList);

        model.addAttribute("menuPage", "/menu/listMenu");
        model.addAttribute("pageName", "식당 정보 확인");
        return "owner/viewRest";
    }

    @GetMapping("viewRest/addMenu")
    public String createMenuForm(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        model.addAttribute("user", loginUser);

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        model.addAttribute("dinning", dinning);

        model.addAttribute("menuPage", "/menu/addMenu");
        model.addAttribute("pageName", "식당 정보 확인");
        return "/owner/viewRest";
    }
    @PostMapping("viewRest/addMenu")
    public String createMenu(Principal principal, Menu menu, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();

        model.addAttribute("user", loginUser);

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        model.addAttribute("dinning", dinning);

        menu.setRestNo(dinning);
        Menu savedMenu = menuService.createMenu(menu);

        return "redirect:/owner/viewRest";
    }

    @GetMapping("viewRest/updateMenu/{menuNo}")
    public String updateMenu(Principal principal, @PathVariable("menuNo") int menuNo, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();

        model.addAttribute("user", loginUser);

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        model.addAttribute("dinning", dinning);

        Menu menu = menuService.getMenuByMenuNo(menuNo);
        model.addAttribute("menu", menu);
        model.addAttribute("menuPage", "/menu/updateMenu");
        model.addAttribute("pageName", "식당 정보 확인");
        return "/owner/viewRest";
    }

    @PostMapping("viewRest/updateMenu/{menuNo}")
    public String updateMenu(Principal principal, @PathVariable("menuNo") int menuNo, Menu Menu, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();

        model.addAttribute("user", loginUser);

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        model.addAttribute("dinning", dinning);

        Menu.setMenuNo(menuNo);
        Menu updateMenu = menuService.updateMenu(Menu);
        return "redirect:/owner/viewRest";
    }

    @GetMapping("viewRest/deleteMenu/{menuNo}")
    public String deleteMenu(Principal principal, @PathVariable("menuNo") int menuNo, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();

        model.addAttribute("user", loginUser);

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        model.addAttribute("dinning", dinning);

        menuService.deleteMenu(menuNo);
        return "redirect:/owner/viewRest";
    }

    // ------------------------------------------------------------------------------------------

    @GetMapping("updateRest")
    public String updateRest(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();

        model.addAttribute("user", loginUser);

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        model.addAttribute("dinning", dinning);
        model.addAttribute("pageName", "식당 정보 수정");
        return "/owner/updateRest";
    }

    @PostMapping("updateRest")
    public String updateRest(Principal principal, Dinning dinning, @RequestParam MultipartFile file) {
        User loginUser = userService.findByUserId( principal.getName() ).get();

        byte[] restImg;
        try {
            restImg = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dinning.setRestImg(restImg);
        dinning.setUserNo(loginUser);
        dinning.setRestStatus(String.valueOf(DinningStatus.NORMAL));
        Dinning updateRestaurant = diningRestService.updateRestaurant(dinning);
        return "redirect:/owner/viewRest";
    }

    @GetMapping("deleteRest")
    public String deleteRestForm(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        model.addAttribute("user", loginUser);
        Dinning dinning = diningRestService.getByUserNo(loginUser);
        if(dinning == null) {
            model.addAttribute("msg", "등록된 가게가 없습니다. 이 기능은 가게 등록 후 사용 가능한 메뉴입니다.");
            model.addAttribute("location", "/owner/addRest");
            return "owner/transit";
        }
        model.addAttribute("dinning", dinning);
        model.addAttribute("pageName", "식당 폐점 신청");
        return "owner/deleteRest";
    }
    @PostMapping("deleteRest") // 삭제하면 외래키 에러 발생
    public String deleteRest(Principal principal, Model model) {
        User loginUser = userService.findByUserId(principal.getName()).get();
        Dinning dinning = diningRestService.getByUserNo(loginUser);

        Dinning updatedDinning = diningRestService.deleteApply(dinning.getRestNo());
        System.out.println(updatedDinning);

        return "redirect:/owner/home";
    }

    // ----------------------- 예약 관련 ------------------------------
    @GetMapping("reservList")
    public String reservList(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        model.addAttribute("user", loginUser);

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        if(dinning == null) {
            model.addAttribute("msg", "등록된 가게가 없습니다. 이 기능은 가게 등록 후 사용 가능한 메뉴입니다.");
            model.addAttribute("location", "/owner/addRest");
            return "owner/transit";
        }
        model.addAttribute("dinning", dinning);


//        List<Reservation> todayReserv = status(reservationRepository.getTodayReservation((long) dinning.getRestNo()));
//        List<Reservation> waitReserv = status(reservationRepository.getWaitReservation((long) dinning.getRestNo()));
//        List<Reservation> pastReserv = status(reservationRepository.getPastReservation((long) dinning.getRestNo()));
//        model.addAttribute("pastReserv", pastReserv);
//        model.addAttribute("waitReserv", waitReserv);
//        model.addAttribute("todayReserv", todayReserv);
        // ModelMapper

//        List<Object[]> reserveListObjObj = reservationRepository.getReservationWithDateType222((long) dinning.getRestNo());
//        System.out.println( reserveListObjObj );
        List<Reservation> reserveList = status(reservationRepository.getReservationWithDateType((long) dinning.getRestNo()));
        reserveList.forEach(Reservation::updateDateType);
        model.addAttribute("reserveList", reserveList);

        model.addAttribute("pageName", "예약 목록");
        return "owner/reservList";
    }

    @GetMapping("resExpired/{resNo}")
    public String resExpired(@PathVariable("resNo") int resNo, Principal principal) {
        User loginUser = userService.findByUserId(principal.getName()).get();
        Reservation reservation = reservationRepository.findById(resNo).get();
        reservation.setRes_status(String.valueOf(ReservationStatus.EXPIRED));
        reservationRepository.save(reservation);
        return "redirect:/owner/reservList";
    }

    @GetMapping("resNoShow/{resNo}")
    public String resNoShow(@PathVariable("resNo") int resNo, Principal principal) {
        User loginUser = userService.findByUserId(principal.getName()).get();
        Reservation reservation = reservationRepository.findById(resNo).get();
        reservation.setRes_status(String.valueOf(ReservationStatus.NO_SHOW));
        reservationRepository.save(reservation);
        return "redirect:/owner/reservList";
    }

    @GetMapping("resCompleted/{resNo}")
    public String resCompleted(@PathVariable("resNo") int resNo, Principal principal) {
        User loginUser = userService.findByUserId(principal.getName()).get();
        Reservation reservation = reservationRepository.findById(resNo).get();
        reservation.setRes_status(String.valueOf(ReservationStatus.RESERVE_COMPLETED));
        reservationRepository.save(reservation);
        return "redirect:/owner/reservList";
    }

    @GetMapping("resCancel/{resNo}/{reason}")
    public String resCancel(@PathVariable("resNo") int resNo, @PathVariable("reason") String reason, Principal principal) {
        User loginUser = userService.findByUserId(principal.getName()).get();
        Reservation reservation = reservationRepository.findById(resNo).get();
        reservation.setRes_status(String.valueOf(ReservationStatus.REST_CANCEL));
        reservation.setRes_rejection_reason(reason);
        reservationRepository.save(reservation);
        return "redirect:/owner/reservList";
    }

    // --------------------- 개인 정보 관리 -----------------------
    @GetMapping("userInfo")
    public String userInfo(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();

        model.addAttribute("user", loginUser);

        model.addAttribute("pageName", "개인 정보 확인");
        return "/owner/userInfo";
    }
    @GetMapping("userUpdate")
    public String userUpdate(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();

        model.addAttribute("user", loginUser);

        model.addAttribute("pageName", "개인 정보 수정");
        return "/owner/userUpdate";
    }
    @PostMapping("userUpdate")
    public String userUpdate(User user, @RequestParam MultipartFile file) {
        User existUser = userService.findByUserNo(user.getUserNo()).get();
        byte[] userImg;
        try {
            userImg = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user.setUserImg(userImg);
        User updateUser = userService.updateUser(user);
        if(!existUser.getUserId().equals(updateUser.getUserId()) || !existUser.getUserPassword().equals(updateUser.getUserPassword())) {
            return "redirect:/logout";
        }
        return "redirect:/owner/userInfo";
    }

    @GetMapping("userDelete")
    public String userDeleteForm(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        model.addAttribute("user", loginUser);
        Dinning dinning = diningRestService.getByUserNo(loginUser);
        model.addAttribute("dinning", dinning);
        model.addAttribute("pageName", "회원 탈퇴 신청");
        return "owner/userDelete";
    }
    @PostMapping("userDelete")
    @Transactional
    public String userDelete(Principal principal, Model model) {
        System.out.println("회원 탈퇴");
        User loginUser = userService.findByUserId(principal.getName()).get();
        Dinning dinning = diningRestService.getByUserNo(loginUser);
        if(dinning != null) {
            model.addAttribute("msg", "가게 폐점 신청이 완료되지 않아 회원 탈퇴가 불가능합니다.");
            model.addAttribute("location", "/owner/deleteRest");
            return "owner/transit";
        }

        DeleteUser deleteUser = new DeleteUser(loginUser.getUserNo(), loginUser.getUserId(), loginUser.getUserAuth(), loginUser.getUserStartDate(), loginUser.isUserBlock());
        deleteUserRepository.save(deleteUser);

        userService.deleteByUserNo(loginUser.getUserNo());
        return "redirect:/logout";
    }

    // ---------------------------- 이벤트 관련 --------------------------
    @GetMapping("eventList")
    public String event(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        model.addAttribute("user", loginUser);
        Dinning dinning = diningRestService.getByUserNo(loginUser);
        if(dinning == null) {
            model.addAttribute("msg", "등록된 가게가 없습니다. 이 기능은 가게 등록 후 사용 가능한 메뉴입니다.");
            model.addAttribute("location", "/owner/addRest");
            return "owner/transit";
        }
        model.addAttribute("dinning", dinning);

        List<Event> eventList = eventService.findByRestNo(dinning);
        model.addAttribute("eventList", eventList);

        model.addAttribute("pageName", "이벤트 목록");
        return "/event/eventList";

    }

    @GetMapping("addEvent")
    public String addEvent(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        model.addAttribute("user", loginUser);
        Dinning dinning = diningRestService.getByUserNo(loginUser);
        if(dinning == null) {
            model.addAttribute("msg", "등록된 가게가 없습니다. 이 기능은 가게 등록 후 사용 가능한 메뉴입니다.");
            model.addAttribute("location", "/owner/addRest");
            return "owner/transit";
        }
        model.addAttribute("dinning", dinning);

        model.addAttribute("pageName", "이벤트 추가");
        return "event/addEvent";
    }

    @PostMapping("addEvent")
    public String addEvent(Principal principal, Event event, Model model, @RequestParam MultipartFile file) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        Dinning dinning = diningRestService.getByUserNo(loginUser);

        byte[] eventImg;
        try {
            eventImg = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        event.setEventImg(eventImg);

        event.setRestNo(dinning);
        eventService.createEvent(event);
        return "redirect:/owner/eventList";
    }

    @GetMapping("viewEvent/{eventNo}")
    public String viewEvent(Principal principal, @PathVariable("eventNo") int eventNo, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        model.addAttribute("user", loginUser);

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        model.addAttribute("dinning", dinning);

        Event event = eventService.findByEventNo(eventNo);
        model.addAttribute("event", event);
        model.addAttribute("pageName", "이벤트 상세보기");
        return "event/viewEvent";
    }

    @GetMapping("updateEvent/{eventNo}")
    public String updateEvent(Principal principal, @PathVariable("eventNo") int eventNo, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        model.addAttribute("user", loginUser);

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        model.addAttribute("dinning", dinning);

        Event event = eventService.findByEventNo(eventNo);
        model.addAttribute("event", event);
        model.addAttribute("pageName", "이벤트 수정");
        return "event/updateEvent";
    }

    @PostMapping("updateEvent/{eventNo}")
    public String updateEvent(Principal principal, @PathVariable("eventNo") int eventNo, Event event, @RequestParam MultipartFile file) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        Dinning dinning = diningRestService.getByUserNo(loginUser);

        byte[] eventImg;
        try {
            eventImg = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        event.setEventImg(eventImg);
        event.setRestNo(dinning);
        eventService.updateEvent(event);
        return "redirect:/owner/viewEvent/" + eventNo;
    }

    @GetMapping("deleteEvent/{eventNo}") // 삭제하면 외래키 에러 발생
    public String deleteEvent(@PathVariable("eventNo") int eventNo) {
        Event event = eventService.findByEventNo(eventNo);
        eventService.deleteEvent(event);
        return "redirect:/owner/eventList";
    }
}