package com.example.mapper;

import com.example.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author dsh
* @description 针对表【menu】的数据库操作Mapper
* @createDate 2025-06-02 07:36:48
* @Entity com.example.entity.Menu
*/
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

}




