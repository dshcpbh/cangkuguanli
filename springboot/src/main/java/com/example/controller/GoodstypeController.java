package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.config.QueryPage;
import com.example.config.Result;
import com.example.entity.Goodstype;
import com.example.entity.Storage;
import com.example.service.GoodstypeService;
import com.example.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author dsh
 */
@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {

    @Autowired
    private GoodstypeService goodstypeService;

    // 查询所有用户
    @GetMapping("/list")
    public Result getAll() {
        List<Goodstype> list = goodstypeService.list();
        return Result.success(list);
    }
    //sava添加用户
    @PostMapping("/save")
    public Result save(@RequestBody Goodstype goodstype) {
        return goodstypeService.save(goodstype)? Result.success() : Result.fail();
    }
    //根据id修改某一个属性
    @PostMapping("/update")
    public Result mod(@RequestBody Goodstype goodstype) {
        return goodstypeService.updateById(goodstype)? Result.success(goodstype) : Result.fail();
    }

    // 根据id查询用户
    @GetMapping("/getById")
    public Goodstype getById(@RequestParam Integer id) {
        return goodstypeService.getById(id);
    }
    //根据id删除用户
    @GetMapping("/delete")
    public Result delete(@RequestParam String id) {
        return goodstypeService.removeById(id)? Result.success() : Result.fail();
    }

    //分页查询
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody QueryPage query){
        System.out.println(query);
        HashMap<String,Object> param=query.getParam();
        String name= (String) param.get("name");
//        String sex= (String) param.get("sex");
//        String roleId= (String) param.get("roleId");
        //设置分页参数
        Page<Goodstype> page=new Page<>();
        //当前页码
        page.setCurrent(query.getPageNum());
        //每页条数
        page.setSize(query.getPageSize());

        //查询条件
        LambdaQueryWrapper<Goodstype> wrapper=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            wrapper.like(Goodstype::getName,name);
        }
//        if(StringUtils.isNotBlank(sex) && !"null".equals(sex)){
//            wrapper.eq(Goodstype::getSex,sex);
//        }
//        if(StringUtils.isNotBlank(roleId) && !"null".equals(roleId)){
//            wrapper.eq(Goodstype::getRoleId,roleId);
//        }

        IPage<Goodstype> result=goodstypeService.page(page,wrapper);
        System.out.println("total:"+result.getTotal());
        return Result.success(result.getRecords(),result.getTotal());
    }
}
