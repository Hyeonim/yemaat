package com.yi.spring.controller;

import com.yi.spring.entity.TablingDto;
import com.yi.spring.repository.TablingRepository;
import com.yi.spring.service.TablingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class TablingController {
    @Autowired
    TablingService tablingService;
    @Autowired
    TablingRepository tablingRepository;

    @GetMapping("list_rest")
    public String restList(Model model) throws Exception {
        List<TablingDto> list = tablingService.getAlltabling();
        model.addAttribute("restList", list);
        return "list_rest";
    }

    @GetMapping("add_rest_form")
    public String restAddForm() throws Exception {
        return "add_rest_form";
    }

    @GetMapping("find_rest_name_form")
    public String findRestNameForm() {
        return "find_rest_name";
    }

    @GetMapping("update_rest_form/{restNo}")
    public String updateRestForm(@PathVariable("restNo") int restNo, Model model) {

        model.addAttribute("rest",tablingService.findByRestNo(restNo));

        return "update_rest_form";
    }


    @GetMapping("rest")
    public String getTabling(Model model) {
        List<TablingDto> list = tablingService.getAlltabling();
        model.addAttribute("restList", list);
        return "list_rest";
    }

    @PostMapping("add_rest")
    public String restAdd(TablingDto dto, Model model) throws Exception {
        tablingService.addTabling(dto);
        return "rest_success";
    }

    @GetMapping("find_rest_name")
    public String findRestName(@RequestParam(name = "keyword") String restName, Model model) {
//        Optional<TablingDto> rest_name = tablingService.findByRestName(restName);
        List<TablingDto> rest_name = tablingRepository.findByRestNameContaining(restName);
        model.addAttribute("find_rest", rest_name);
        return "find_rest_name";
    }
    @PostMapping("update_rest")
    public String updateRest(@ModelAttribute TablingDto tablingDto) {
        tablingRepository.updateByRestNo(tablingDto);
        return "redirect:/list_rest";
    }
}
