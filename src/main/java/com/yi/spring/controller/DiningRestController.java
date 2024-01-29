package com.yi.spring.controller;

import com.yi.spring.entity.DiningRest;
import com.yi.spring.service.DiningRestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rest")
public class DiningRestController {
    @Autowired
    private DiningRestService diningRestService;


    @GetMapping
    public ResponseEntity<List<DiningRest>> getAllRestaurants() {
        List<DiningRest> diningRestList = diningRestService.getAllRestaurants();
        return new ResponseEntity<>(diningRestList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DiningRest> createRestaurant(@RequestBody DiningRest diningRest) {
        DiningRest savedDiningRest = diningRestService.createRestaurant(diningRest);
        savedDiningRest.setUser_no(0);
        return new ResponseEntity<>(savedDiningRest, HttpStatus.CREATED);
    }

    @PutMapping("{rest_no}")
    public ResponseEntity<DiningRest> updateRestaurant(@PathVariable("rest_no") int rest_no, @RequestBody DiningRest diningRest) {
        diningRest.setRest_no(rest_no);
        DiningRest updateRestaurant = diningRestService.updateRestaurant(diningRest);
        return new ResponseEntity<>(updateRestaurant, HttpStatus.OK);
    }

    @DeleteMapping("{rest_no}")
    public  ResponseEntity<String> deleteRestaurant(@PathVariable("rest_no") int rest_no) {
        diningRestService.deleteRestaurant(rest_no);
        return new ResponseEntity<>("Restaurant successfully delete!", HttpStatus.OK);
    }
}
