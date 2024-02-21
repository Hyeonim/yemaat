package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.repository.DinningRepository;
import com.yi.spring.service.DinningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class ListController {

    @Autowired
    private DinningService dinningService;

    @Autowired
    private DinningRepository dinningRepository;


    public ListController(DinningService dinningService){
        this.dinningService = dinningService;
    }

    @GetMapping("search")
    public String map(Model model) {
       List<Dinning> list = dinningRepository.findAll();
//
//        for (Dinning diningRest : list) {
//            System.out.println("Latitude: " + diningRest.getRestLatitude() + ", Longitude: " + diningRest.getRestLongitude() + "가게 이름" + diningRest.getRestName());
//        }

//        List<Dinning> respList = new ArrayList<>();
//        for (Dinning diningRest : list) {
//            Dinning elem = new Dinning();
//            elem.setRestLatitude( diningRest.getRestLatitude());
//            elem.setRestLongitude( diningRest.getRestLongitude());
//            elem.setRestName( diningRest.getRestName());
//            elem.setRestNo(diningRest.getRestNo());
//            respList.add(elem);
//        }


        model.addAttribute("list", list);

        List<Dinning> listOrderByRestScore = dinningRepository.getRestScore();
        model.addAttribute("listOrderByRestScore", listOrderByRestScore);

        return "search";
    }

    @GetMapping("/find_rest_name")
    public String findRestName(@RequestParam(name = "keyword") String restName, Model model) {
        System.out.println("Controller method called with keyword: " + restName);

//        List<Dinning> list = dinningRepository.findAll();
//
//        List<Dinning> respList = new ArrayList<>();
//        for (Dinning diningRest : list) {
//            Dinning elem = new Dinning();
//            elem.setRestLatitude( diningRest.getRestLatitude());
//            elem.setRestLongitude( diningRest.getRestLongitude());
//            elem.setRestName( diningRest.getRestName());
//            elem.setRestNo(diningRest.getRestNo());
//            respList.add(elem);
//        }

        List<Dinning> rest_name = dinningRepository.findByRestNameContaining(restName);
//        List<DinningDto> rest_name = dinningRepository.findByRestNameContainingFromView(restName);
//        List<Object[]> rest_name2 = dinningRepository.findByRestNameContainingFromView(restName);
//        List<DinningDto> rest_name = rest_name2.stream().map(DinningDto::new).toList();

        model.addAttribute("list", rest_name);


//        List<Object[]> listOrderByRestScore2 = dinningRepository.getRestScore2();
//        List<Dinning> listOrderByRestScore = new ArrayList<>();
//        for ( Object[] items : listOrderByRestScore2 )
//        {
//            Dinning elem = (Dinning)items[0];
//            elem.setRestScore( (Double)items[1] );
//            listOrderByRestScore.add( elem );
//        }

//        model.addAttribute("listOrderByRestScore", listOrderByRestScore);
        return "search";
    }

//    @GetMapping("/find_rest_name_x")
//    public ResponseEntity<List<Dinning>> findRestName_x(@RequestParam(name = "keyword") String restName, Model model) {
//        List<Dinning> rest_name = dinningRepository.findByRestNameContaining(restName);
//        System.out.println( rest_name );
//        return new ResponseEntity<>(rest_name, HttpStatus.OK);
//    }
}
