package com.yi.spring.service;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Menu;
import com.yi.spring.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService{
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> getMenusByRestNo(Integer restNo) {
        return menuRepository.findMenusByRestNo( new Dinning( restNo ) );
    }

    @Override
    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public Menu updateMenu(Menu menu) {
        Menu existingMenu = menuRepository.findById(menu.getMenuNo()).orElse(null);
        assert existingMenu != null;

        existingMenu.setMenuName(menu.getMenuName());
        existingMenu.setMenuPrice(menu.getMenuPrice());
        existingMenu.setRestNo(menu.getRestNo());

        return menuRepository.save(existingMenu);
    }

    @Override
    public void deleteMenu(Integer menuNo) {
        menuRepository.deleteById(menuNo);
    }
}
