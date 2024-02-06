package com.yi.spring.controller;

import com.yi.spring.entity.*;
import com.yi.spring.repository.ReservationRepository;
import com.yi.spring.service.DiningRestService;
import com.yi.spring.service.EventService;
import com.yi.spring.service.MenuService;
import com.yi.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("home")
    public String home(Model model) {
        User user = userService.findByUserNo(1).get();
        model.addAttribute("user", user);

        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);
        return "owner/home";
    }

    @GetMapping("addRest")
    public String addRest(Model model) {
        User user = userService.findByUserNo(1).get();
        model.addAttribute("user", user);
        return "/owner/addRest";
    }

    @PostMapping("addRest") // 등록 화면으로 전환은 되는데 등록은 안됨
    public String addRest(Dinning dinning, Model model) {
        User user = userService.findByUserNo(1).get();

        dinning.setUserNo(user);
        diningRestService.createRestaurant(dinning);
        return "redirect:/owner/home";
    }

    // -------------------------- 가게 상세보기 및 메뉴 관리 -----------------------------------------
    @GetMapping("viewRest")
    public String viewRest(Model model) {
        User user = userService.findByUserNo(1).get();
        model.addAttribute("user", user);

        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);


        List<Menu> menuList = menuService.getMenusByRestNo(dinning.getRestNo());
        model.addAttribute("menuList", menuList);

        model.addAttribute("menuPage", "/menu/listMenu");
        return "owner/viewRest";
    }

    @GetMapping("viewRest/addMenu")
    public String createMenuForm(Model model) {
        User user = userService.findByUserNo(1).get();
        model.addAttribute("user", user);

        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);

        model.addAttribute("menuPage", "/menu/addMenu");
        return "/owner/viewRest";
    }
    @PostMapping("viewRest/addMenu")
    public String createMenu(Menu menu, Model model) {
        User user = userService.findByUserNo(1).get();
        model.addAttribute("user", user);

        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);

        menu.setRestNo(dinning);
        Menu savedMenu = menuService.createMenu(menu);

        return "redirect:/owner/viewRest";
    }

    @GetMapping("viewRest/updateMenu/{menuNo}")
    public String updateMenu(@PathVariable("menuNo") int menuNo, Model model) {
        User user = userService.findByUserNo(1).get();
        model.addAttribute("user", user);

        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);

        Menu menu = menuService.getMenuByMenuNo(menuNo);
        model.addAttribute("menu", menu);
        model.addAttribute("menuPage", "/menu/updateMenu");
        return "/owner/viewRest";
    }

    @PostMapping("viewRest/updateMenu/{menuNo}")
    public String updateMenu(@PathVariable("menuNo") int menuNo, Menu Menu, Model model) {
        User user = userService.findByUserNo(1).get();
        model.addAttribute("user", user);

        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);

        Menu.setMenuNo(menuNo);
        Menu updateMenu = menuService.updateMenu(Menu);
        return "redirect:/owner/viewRest";
    }

    @GetMapping("viewRest/deleteMenu/{menuNo}")
    public String deleteMenu(@PathVariable("menuNo") int menuNo, Model model) {
        User user = userService.findByUserNo(1).get();
        model.addAttribute("user", user);

        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);

        menuService.deleteMenu(menuNo);
        return "redirect:/owner/viewRest";
    }

    // ------------------------------------------------------------------------------------------

    @GetMapping("updateRest")
    public String updateRest(Model model) {
        User user = userService.findByUserNo(1).get();
        model.addAttribute("user", user);

        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);
        return "/owner/updateRest";
    }

    @PostMapping("updateRest")
    public String updateRest(Dinning dinning) {
        User user = userService.findByUserNo(1).get();

        dinning.setUserNo(user);
        Dinning updateRestaurant = diningRestService.updateRestaurant(dinning);
        return "redirect:/owner/viewRest";
    }

    @GetMapping("deleteRest") // 삭제하면 외래키 에러 발생
    public String deleteRest(Model model) {
        User user = userService.findByUserNo(1).get();

        Dinning dinning = diningRestService.getByUserNo(user);

        diningRestService.deleteRestaurant(dinning.getRestNo());
        return "redirect:/owner/home";
    }

    // ----------------------- 예약 관련 ------------------------------
    @GetMapping("reservation")
    public String reservation(Model model) {
        User user = userService.findByUserNo(1).get();

        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);


        List<Reservation> reservList = reservationRepository.findByRestNo((long) dinning.getRestNo());
        model.addAttribute("reservList", reservList);

        return "/owner/reservList";
    }

    // --------------------- 개인 정보 관리 -----------------------
    @GetMapping("userInfo")
    public String userInfo(Model model) {
        User user = userService.findByUserNo(1).get();
        model.addAttribute("user", user);

        return "/owner/userInfo";
    }
    @GetMapping("userUpdate")
    public String userUpdate(Model model) {
        User user = userService.findByUserNo(1).get();
        model.addAttribute("user", user);

        return "/owner/userUpdate";
    }
    @PostMapping("userUpdate")
    public String userUpdate(User user) {
        System.out.println(user);
        User updateUser = userService.updateUser(user);
        return "redirect:/owner/userInfo";
    }

    @GetMapping("userDelete")
    public void userDelete(User user) {
        // 회원 탈퇴 기능
    }

    // ---------------------------- 이벤트 관련 --------------------------
    @GetMapping("eventList")
    public String event(Model model) {
        User user = userService.findByUserNo(1).get();
        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);

        List<Event> eventList = eventService.findByRestNo(dinning);
        model.addAttribute("eventList", eventList);

        return "/event/eventList";

    }

    @GetMapping("addEvent")
    public String addEvent(Model model) {
        User user = userService.findByUserNo(1).get();
        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);
        return "event/addEvent";
    }

    @PostMapping("addEvent")
    public String addEvent(Event event, Model model) {
        User user = userService.findByUserNo(1).get();
        Dinning dinning = diningRestService.getByUserNo(user);

        event.setRestNo(dinning);
        eventService.createEvent(event);
        return "redirect:/owner/eventList";
    }

    @GetMapping("viewEvent/{eventNo}")
    public String viewEvent(@PathVariable("eventNo") int eventNo, Model model) {
        User user = userService.findByUserNo(1).get();

        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);

        Event event = eventService.findByEventNo(eventNo);
        model.addAttribute("event", event);
        return "event/viewEvent";
    }

    @GetMapping("updateEvent/{eventNo}")
    public String updateEvent(@PathVariable("eventNo") int eventNo, Model model) {
        User user = userService.findByUserNo(1).get();

        Dinning dinning = diningRestService.getByUserNo(user);
        model.addAttribute("dinning", dinning);

        Event event = eventService.findByEventNo(eventNo);
        model.addAttribute("event", event);
        return "event/updateEvent";
    }

    @PostMapping("updateEvent/{eventNo}")
    public String updateEvent(@PathVariable("eventNo") int eventNo, Event event) {
        User user = userService.findByUserNo(1).get();
        Dinning dinning = diningRestService.getByUserNo(user);

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