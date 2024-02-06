package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Menu;
import com.yi.spring.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/menuMining")
public class MenuTextMining {
    @Autowired
    MenuRepository menuRepository;
    @GetMapping("/")
    public String mining(Model model){


        List<Menu> list = menuRepository.findAll();
        Map<String, Integer> wordFrequency = new HashMap<>();

        for ( Menu item : list )
        {
            String cleanText = item.getMenuName().replaceAll("[^가-힣]", "");
            String[] words = cleanText.split("");

            for (String word : words) {
                // 단어가 이미 등록되어 있으면 빈도수 증가, 없으면 새로 등록
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
            }
        }

        class _TempDto {
            public String menuName;
            public String frequency = "";
        }
        List< _TempDto > freqList = new ArrayList<>();


        for ( Menu item : list )
        {
            for (char c : item.getMenuName().toCharArray()) {
                String character = String.valueOf(c);
                int frequency = wordFrequency.getOrDefault(character, 0);

                if (0 < frequency) {
                    _TempDto itemData = new _TempDto();
                    itemData.menuName = item.getMenuName();
                    itemData.frequency += character + ":" + frequency + " ";
                    freqList.add( itemData );
                }
            }
        }




        model.addAttribute( "list", wordFrequency );
        model.addAttribute( "freq", freqList );
        return "/miningTest";
    }
}
