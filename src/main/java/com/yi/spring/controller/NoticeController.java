package com.yi.spring.controller;

import com.yi.spring.entity.Notice;
import com.yi.spring.repository.NoticeRepository;
import com.yi.spring.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @Autowired
    NoticeRepository noticeRepository;

    @GetMapping("/noticeList")
    public String managerPage(Model model,
                              @RequestParam(value = "page", defaultValue = "0") int page) {

        List<Notice> head = noticeRepository.findByImportantNotice(true);

        Page<Notice> noticeList = noticeService.findAll(page);


        model.addAttribute("list", noticeList); // 수정된 부분: 검색 결과를 담도록 변경
        model.addAttribute("head", head); // 수정된 부분: 검색 결과를 담도록 변경


        return "notice";
    }

}
