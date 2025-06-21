package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.config.QueryPage;
import com.example.config.Result;
import com.example.entity.Book;
import com.example.entity.Record;
import com.example.service.BookService;
import com.example.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author dsh
 */
@RestController
@RequestMapping("/records")
public class RecordController {

    @Autowired
    private RecordService  recordService;

    @Autowired
    private BookService bookService;




    // 查询所有用户
    @GetMapping("/list")
    public Result getAll() {
        List<Record> list = recordService.list();
        return Result.success(list);
    }
    //sava添加用户
    @PostMapping("/save")
    public Result save(@RequestBody Record record) {
        Book book = bookService.getById(record.getBook());
        int n = record.getCount();
        if("2".equals( record.getAction())){
            n= -n;
            record.setCount(n);
        }
        int num = book.getCount() + n;
        book.setCount(num);
        bookService.updateById(book);
        return recordService.save(record)? Result.success() : Result.fail();
    }
    //根据id修改某一个属性
    @PostMapping("/update")
    public Result mod(@RequestBody Record record) {
        return recordService.updateById(record)? Result.success(record) : Result.fail();
    }

    // 根据id查询用户
    @GetMapping("/getById")
    public Record getById(@RequestParam Integer id) {
        return recordService.getById(id);
    }
    //根据id删除用户
    @GetMapping("/delete")
    public Result delete(@RequestParam String id) {
        return recordService.removeById(id)? Result.success() : Result.fail();
    }

    //分页查询
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody QueryPage query){
        System.out.println(query);
        HashMap<String,Object> param=query.getParam();
        String name= (String) param.get("name");
        String  storage = (String) param.get("storage");
        String  goodstype = (String) param.get("goodstype");
        String  userid = (String) param.get("userid");
        String  roleId = (String) param.get("roleId");
        //设置分页参数
        Page<Record> page=new Page<>();
        //当前页码
        page.setCurrent(query.getPageNum());
        //每页条数
        page.setSize(query.getPageSize());


        //设置查询条件
        QueryWrapper<Record> QueryWrapper = new QueryWrapper<>();
//        System.out.println(roleId);

        if("2".equals(roleId)){
            QueryWrapper.eq("r.userid", userid);
        }

        if(StringUtils.isNotEmpty(name) && !"null".equals(name)){
            QueryWrapper.like("b.name", name);
        }
        if(StringUtils.isNotEmpty(storage) && !"null".equals(storage)){
            QueryWrapper.eq("s.id", storage);
        }
        if(StringUtils.isNotEmpty(goodstype) && !"null".equals(goodstype)){
            QueryWrapper.eq("gt.id", goodstype);
        }
        System.out.println("Generated SQL: " + QueryWrapper.getTargetSql());


        //执行查询
        IPage<Record> result = recordService.pagec(page, QueryWrapper);
        System.out.println("total:"+result.getTotal());
        return Result.success(result.getRecords(),result.getTotal());
    }
}
