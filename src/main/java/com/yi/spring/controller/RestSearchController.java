package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.meta.DinningReviewView;
import com.yi.spring.repository.DinningWithReviewRepository;
import com.yi.spring.repository.DinningReviewSpecifications;
import com.yi.spring.repository.DinningRepository;
import com.yi.spring.repository.ReviewRepository;
import com.yi.spring.service.DinningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/")
public class RestSearchController {

    @Autowired
    private DinningService dinningService;

    @Autowired
    private DinningRepository dinningRepository;
    @Autowired
    private DinningWithReviewRepository dinningWithReviewRepository;


    public RestSearchController(DinningService dinningService){
        this.dinningService = dinningService;
    }

//    @GetMapping("search")
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

        System.out.println("Controller method called with keyword: " + "restName");



//        List<DinningDto> rest_name = dinningRepository.findByRestNameContainingFromView(restName);
//        List<Object[]> rest_name2 = dinningRepository.findByRestNameContainingFromView(restName);
//        List<DinningDto> rest_name = rest_name2.stream().map(DinningDto::new).toList();



//        List<Object[]> listOrderByRestScore2 = dinningRepository.getRestScore2();
//        List<Dinning> listOrderByRestScore = new ArrayList<>();
//        for ( Object[] items : listOrderByRestScore2 )
//        {
//            Dinning elem = (Dinning)items[0];
//            elem.setRestScore( (Double)items[1] );
//            listOrderByRestScore.add( elem );
//        }


        model.addAttribute("list", list);

//        List<Dinning> listOrderByRestScore = dinningRepository.getRestScore();
//        model.addAttribute("listOrderByRestScore", listOrderByRestScore);

        return "search";
    }

//    @GetMapping("/find_rest_name_x")
//    public ResponseEntity<List<Dinning>> findRestName_x(@RequestParam(name = "keyword") String restName, Model model) {
//        List<Dinning> rest_name = dinningRepository.findByRestNameContaining(restName);
//        System.out.println( rest_name );
//        return new ResponseEntity<>(rest_name, HttpStatus.OK);
//    }


    @GetMapping("/search")
    public String findRestName(@RequestParam(name = "keyword", defaultValue="") String restName, Model model,
                               @RequestParam Map<String, String> params) {
        List<Dinning> restList = new ArrayList<>();
        boolean bActionDefault = true;

        System.out.println( params );
        String filter1 = params.get( "filter1" );
        if ( null != filter1 )
        {
            switch (filter1) {
                /*
                case "111": //-> System.out.println( 11 );
                {
                    List<Object[]> listOrderByRestScore2 = dinningRepository.getRestScore2();
                    List<Dinning> listOrderByRestScore = new ArrayList<>();
                    for ( Object[] items : listOrderByRestScore2 )
                    {
                        Dinning elem = (Dinning)items[0];
                        elem.setRestScore( (Double)items[1] );
                        listOrderByRestScore.add( elem );
                    }
                    restList = listOrderByRestScore;
                }
                bActionDefault = false;
                break;
                case "222": //-> System.out.println( 12 );
                {
                    if(restName == null || restName.isEmpty()) {
                        restList = dinningRepository.findAllWithTotalReviewsOrderByTotalReviewsDesc();
                    }
                    else {
                        restList = dinningRepository.findAllWithTotalReviewsOrderByTotalReviewsDesc(restName);
                    }
                }
                bActionDefault = false;
                break;
                */
                case "1": //-> System.out.println( 13 );
                case "2": //-> System.out.println( 13 );
                case "3": //-> System.out.println( 13 );
                {
                    final Map<String, String> sortStrategy = Map.of(
                            "1","restScore2"
                            , "2","totalReviews"
                            , "3","reserveCount");
                    Matcher matcher = Pattern.compile(
                            "([123])"
                    ).matcher( filter1 );

                    List<String> mySort = new ArrayList<>();
                    while (matcher.find()) {
                        mySort.add( sortStrategy.get( matcher.group() ));
                    }

                    Specification<DinningReviewView> spec = Specification
                        .where(DinningReviewSpecifications.likeRestName( restName ))
                        .and(DinningReviewSpecifications.eqCategory(null))
                        .and(DinningReviewSpecifications.likeAddr(null))
                    ;

                    List<DinningReviewView> dinningReviewList =
                            dinningWithReviewRepository.findAll( spec, Sort.by( Sort.Direction.DESC, mySort.toArray(new String[0]) ));
//                    System.out.println( dinningReviewList );
//                    org.thymeleaf.spring6.expression.Fields
                    model.addAttribute("list", dinningReviewList);
                }
                bActionDefault = false;
                break;
                default :// System.out.println("Unexpected value: " + filter1);
            }
        }
        if ( bActionDefault )
        {
            restList = dinningRepository.findByRestNameContaining(restName);
            model.addAttribute("list", restList);
        }


        return "search";
    }
}
