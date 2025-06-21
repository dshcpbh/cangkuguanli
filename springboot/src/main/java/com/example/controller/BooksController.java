package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.config.QueryPage;
import com.example.config.Result;
import com.example.entity.Book;
import com.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author dsh
 */
@RestController
@RequestMapping("/book")
public class BooksController {

    @Autowired
    private BookService bookService;

    // 查询所有用户
    @GetMapping("/list")
    public Result getAll() {
        List<Book> list = bookService.list();
        return Result.success(list);
    }
    //sava添加用户
    @PostMapping("/save")
    public Result save(@RequestBody Book book) {
        return bookService.save(book)? Result.success() : Result.fail();
    }
    //根据id修改某一个属性
    @PostMapping("/update")
    public Result mod(@RequestBody Book book) {
        return bookService.updateById(book)? Result.success(book) : Result.fail();
    }

    // 根据id查询用户
    @GetMapping("/getById")
    public Book getById(@RequestParam Integer id) {
        return bookService.getById(id);
    }
    //根据id删除用户
    @GetMapping("/delete")
    public Result delete(@RequestParam String id) {
        return bookService.removeById(id)? Result.success() : Result.fail();
    }

    //分页查询
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody QueryPage query){
        System.out.println(query);
        HashMap<String,Object> param=query.getParam();
        String name= (String) param.get("name");
        String  storage = (String) param.get("storage");
        String  goodstype = (String) param.get("goodstype");
        //设置分页参数
        Page<Book> page=new Page<>();
        //当前页码
        page.setCurrent(query.getPageNum());
        //每页条数
        page.setSize(query.getPageSize());

        //查询条件
        LambdaQueryWrapper<Book> wrapper=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            wrapper.like(Book::getName,name);
        }
        if(StringUtils.isNotBlank(storage) && !"null".equals(storage)){
            wrapper.eq(Book::getStorage,storage);
        }
        if(StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)){
            wrapper.eq(Book::getGoodstype,goodstype);
        }

        IPage<Book> result=bookService.page(page,wrapper);
        System.out.println("total:"+result.getTotal());
        return Result.success(result.getRecords(),result.getTotal());
    }
}
