package com.yi.spring.controller;

import com.yi.spring.entity.TablingDto;
import com.yi.spring.entity.User;
import com.yi.spring.service.TablingService;
import com.yi.spring.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class TablingAPIController {

//    @Autowired
//    TablingService tablingService;
//
//    @GetMapping("/tabling")
//    public ResponseEntity<List<TablingDto>> getAlltabling(){
//        List<TablingDto> tabling = tablingService.getAlltabling();
//        return new ResponseEntity<>(tabling, HttpStatus.OK);
//    }
}
