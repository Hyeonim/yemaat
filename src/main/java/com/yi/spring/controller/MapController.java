package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.service.DinningService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/map/*")
public class MapController {

    private DinningService dinningService;

    public MapController(DinningService dinningService){
        this.dinningService = dinningService;
    }


    @GetMapping("view")
    public String map(Model model) {
       List<Dinning> list = dinningService.findAll();

        for (Dinning diningRest : list) {
            System.out.println("Latitude: " + diningRest.getRestLatitude() + ", Longitude: " + diningRest.getRestLongitude() + "가게 이름" + diningRest.getRestName());
        }

        List<Dinning> respList = new ArrayList<>();
        for (Dinning diningRest : list) {
            Dinning elem = new Dinning();
            elem.setRestLatitude( diningRest.getRestLatitude());
            elem.setRestLongitude( diningRest.getRestLongitude());
            elem.setRestName( diningRest.getRestName());
            respList.add(elem);
        }


        model.addAttribute("list", respList);

        return "map";
    }
}
