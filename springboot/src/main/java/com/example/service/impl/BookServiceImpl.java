package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Book;
import com.example.service.BookService;
import com.example.mapper.BookMapper;
import org.springframework.stereotype.Service;

/**
* @author dsh
* @description 针对表【book】的数据库操作Service实现
* @createDate 2025-06-05 22:34:50
*/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

}




