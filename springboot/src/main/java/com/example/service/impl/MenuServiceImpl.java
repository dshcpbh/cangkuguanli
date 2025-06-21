package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Menu;
import com.example.service.MenuService;
import com.example.mapper.MenuMapper;
import org.springframework.stereotype.Service;

/**
* @author dsh
* @description 针对表【menu】的数据库操作Service实现
* @createDate 2025-06-02 07:36:48
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

}




