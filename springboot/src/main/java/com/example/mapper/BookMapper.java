package com.example.mapper;

import com.example.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author dsh
* @description 针对表【book】的数据库操作Mapper
* @createDate 2025-06-05 22:34:50
* @Entity com.example.entity.Book
*/
@Mapper
public interface BookMapper extends BaseMapper<Book> {

}




