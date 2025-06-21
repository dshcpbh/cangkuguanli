package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.config.QueryPage;
import com.example.config.Result;
import com.example.entity.Storage;
import com.example.entity.User;
import com.example.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author dsh
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    // 查询所有用户
    @GetMapping("/list")
    public Result getAll() {
        List<Storage> list = storageService.list();
        return Result.success(list);
    }
    //sava添加用户
    @PostMapping("/save")
    public Result save(@RequestBody Storage storage) {
        return storageService.save(storage)? Result.success() : Result.fail();
    }
    //根据id修改某一个属性
    @PostMapping("/update")
    public Result mod(@RequestBody Storage storage) {
        return storageService.updateById(storage)? Result.success(storage) : Result.fail();
    }

    // 根据id查询用户
    @GetMapping("/getById")
    public Storage getById(@RequestParam Integer id) {
        return storageService.getById(id);
    }
    //根据id删除用户
    @GetMapping("/delete")
    public Result delete(@RequestParam String id) {
        return storageService.removeById(id)? Result.success() : Result.fail();
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
        Page<Storage> page=new Page<>();
        //当前页码
        page.setCurrent(query.getPageNum());
        //每页条数
        page.setSize(query.getPageSize());

        //查询条件
        LambdaQueryWrapper<Storage> wrapper=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            wrapper.like(Storage::getName,name);
        }
//        if(StringUtils.isNotBlank(sex) && !"null".equals(sex)){
//            wrapper.eq(Storage::getSex,sex);
//        }
//        if(StringUtils.isNotBlank(roleId) && !"null".equals(roleId)){
//            wrapper.eq(Storage::getRoleId,roleId);
//        }

        IPage<Storage> result=storageService.page(page,wrapper);
        System.out.println("total:"+result.getTotal());
        return Result.success(result.getRecords(),result.getTotal());
    }
}
