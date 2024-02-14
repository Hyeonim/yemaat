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

import java.security.Principal;
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
        model.addAttribute("pageName", "식당 등록");
        return "/owner/addRest";
    }

    @PostMapping("addRest") // 등록 화면으로 전환은 되는데 등록은 안됨
    public String addRest(Principal principal, Dinning dinning, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
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
    public String updateRest(Principal principal, Dinning dinning) {
        User loginUser = userService.findByUserId( principal.getName() ).get();

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
    @GetMapping("reservation")
    public String reservation(Principal principal, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();

        Dinning dinning = diningRestService.getByUserNo(loginUser);
        if(dinning == null) {
            model.addAttribute("msg", "등록된 가게가 없습니다. 이 기능은 가게 등록 후 사용 가능한 메뉴입니다.");
            model.addAttribute("location", "/owner/addRest");
            return "owner/transit";
        }
        model.addAttribute("dinning", dinning);


//        List<Reservation> reservList = reservationRepository.findByRestNo((long) dinning.getRestNo());
//        model.addAttribute("reservList", reservList);

        model.addAttribute("pageName", "예약 목록");
        return "/owner/reservList";
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
    public String userUpdate(User user) {
        User existUser = userService.findByUserNo(user.getUserNo()).get();
        User updateUser = userService.updateUser(user);
        if(existUser.getUserId().equals(updateUser.getUserId()) || existUser.getUserPassword().equals(updateUser.getUserPassword())) {
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
    public String addEvent(Principal principal, Event event, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        Dinning dinning = diningRestService.getByUserNo(loginUser);

        event.setRestNo(dinning);
        eventService.createEvent(event);
        return "redirect:/owner/eventList";
    }

    @GetMapping("viewEvent/{eventNo}")
    public String viewEvent(Principal principal, @PathVariable("eventNo") int eventNo, Model model) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
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
        Dinning dinning = diningRestService.getByUserNo(loginUser);
        model.addAttribute("dinning", dinning);

        Event event = eventService.findByEventNo(eventNo);
        model.addAttribute("event", event);
        model.addAttribute("pageName", "이벤트 수정");
        return "event/updateEvent";
    }

    @PostMapping("updateEvent/{eventNo}")
    public String updateEvent(Principal principal, @PathVariable("eventNo") int eventNo, Event event) {
        User loginUser = userService.findByUserId( principal.getName() ).get();
        Dinning dinning = diningRestService.getByUserNo(loginUser);

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