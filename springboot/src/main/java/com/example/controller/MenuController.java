package com.example.controller;

import com.example.config.Result;
import com.example.entity.Menu;
import com.example.service.MenuService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dsh
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("/list")
    public Result list(@RequestParam String roleId){
        List<Menu> menuList = menuService.lambdaQuery().like(Menu::getMenuright,roleId).list();
//        System.out.println(menuList);
        return !menuList.isEmpty() ? Result.success(menuList)  : Result.fail();
    }
}
