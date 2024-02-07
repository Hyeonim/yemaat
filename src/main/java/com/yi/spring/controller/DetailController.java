package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Review;
import com.yi.spring.entity.User;
import com.yi.spring.repository.DinningRepository;
import com.yi.spring.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import java.time.LocalTime;

@Controller
@RequestMapping("/*")
public class DetailController {

    @Autowired
    DinningRepository dinningRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @GetMapping("/detail")
    public void getDinningByRestNo(@RequestParam Long restNo, Model model) {


        Optional<Dinning> dinningOptional = dinningRepository.findById((restNo));

        List<Review> list = reviewRepository.findByRestNo(new Dinning(Math.toIntExact(restNo)));

        double sum = list.stream().mapToDouble(Review::getRevScore).sum();
        double result = sum /list.size();



        dinningOptional.ifPresent(dinning -> model.addAttribute("dinning", dinning));

        model.addAttribute("list",list);
        model.addAttribute("reg", result);

//        return "detail";

    }

    @PostMapping("/reviewAdd")
    public void reviewAdd(@RequestParam MultipartFile file, Review review) {
        if (file.isEmpty()) {
            reviewRepository.save(review);
        } else {
            try {
                byte[] revImg = file.getBytes();
                review.setRevImg(revImg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        reviewRepository.save(review);
    }

    @GetMapping("reviewTest")
    public void reviewTest(){
        // processFilesInFolder( "C:/Users/lenovo/Documents//img" );
    }
//    public void processFilesInFolder(String folderPath) {
//
//
//        File folder = new File(folderPath);
//        File[] files = folder.listFiles();
//
//        if (files != null) {
//            for (File file : files) {
//                // 파일 처리 작업 수행
//                System.out.println("Processing file: " + file.getName());
//                try {
//                    byte[] revImg = StreamUtils.copyToByteArray(new FileInputStream(file));
//
//
//                    Review review = new Review();
//                    review.setRevContent( "이미지 업로드 테스트" );
//                    review.setRevScore( 50 );
//                    review.setRevWriteTime( LocalTime.now().toString() );
//                    review.setRevImg(revImg);
//                    reviewRepository.save(review);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        } else {
//            System.err.println("Folder is empty or does not exist: " + folderPath);
//        }
//    }
//


}
