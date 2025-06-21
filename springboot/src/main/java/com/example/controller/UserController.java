package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.config.QueryPage;
import com.example.config.Result;
import com.example.entity.Menu;
import com.example.entity.User;
import com.example.service.MenuService;
import com.example.service.UserService;
import com.example.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    @GetMapping("/checkNo")
    public Result checkNo (@RequestParam String no){
        List<User> list = userService.lambdaQuery().eq(User::getNo,no).list();
        return !list.isEmpty() ? Result.success(list)  : Result.fail();
    }

    // 查询所有用户
    @GetMapping("/list")
    public Result getAll() {
        List<User> list = userService.list();
        return Result.success(list);
    }
    //sava添加用户
    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        return userService.save(user)? Result.success() : Result.fail();
    }
    //根据id修改某一个属性
    @PostMapping("/update")
    public Result mod(@RequestBody User user) {
        return userService.updateById(user)? Result.success(user) : Result.fail();
    }

    // 根据id查询用户
    @GetMapping("/getById")
    public User getById(@RequestParam Integer id) {
        return userService.getById(id);
    }
    //根据id删除用户
    @GetMapping("/delete")
    public Result delete(@RequestParam String id) {
        return userService.removeById(id)? Result.success() : Result.fail();
    }

    //分页查询
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody QueryPage query){
        System.out.println(query);
        HashMap<String,Object> param=query.getParam();
        String name= (String) param.get("name");
        String sex= (String) param.get("sex");
        String roleId= (String) param.get("roleId");

        //设置分页参数
        Page<User> page=new Page<>();
        //当前页码
        page.setCurrent(query.getPageNum());
        //每页条数
        page.setSize(query.getPageSize());

        //查询条件
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            wrapper.like(User::getName,name);
        }
        if(StringUtils.isNotBlank(sex) && !"null".equals(sex)){
            wrapper.eq(User::getSex,sex);
        }
        if(StringUtils.isNotBlank(roleId) && !"null".equals(roleId)){
            wrapper.eq(User::getRoleId,roleId);
        }

        IPage<User> result=userService.page(page,wrapper);
        System.out.println("total:"+result.getTotal());
        return Result.success(result.getRecords(),result.getTotal());
    }
    //分页查询
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        if (StringUtils.isBlank(user.getNo()) || StringUtils.isBlank(user.getPassword())){
            return Result.fail();
        }
        //查询条件
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getNo,user.getNo())
                .eq(User::getPassword,user.getPassword());

        List<User> list=userService.list(wrapper);
//        System.out.println(list);
        if(!list.isEmpty()){
            //生成token
            Map<String, String> map=new HashMap<>();
            map.put("no",list.getFirst().getNo());
            map.put("password",list.getFirst().getPassword());
            String token= JWTUtils.getToken(map);

            if (token == null || token.isEmpty()) {
                log.error("生成的 token 为空");
                return Result.fail();
            }
            User  user1=list.getFirst();
            List<Menu> menuList=menuService.lambdaQuery().like(Menu::getMenuright,list.getFirst().getRoleId()).list();

            user1.setToken(token);
            HashMap<String,Object> res=new HashMap<>();
            res.put("user",user1);
            res.put("menu",menuList);
            return Result.success(res);

        }else{
            return Result.fail();
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
//        const registerForm = ref({
//                no: '',
//                name: '',
//                sex: '1',
//                age: '0',
//                roleId: '1',
//                password: '',
//                confirmPassword: '',
//                txpath:'http://localhost:8080/files/download/OIP.jpg'
//})
        //验证no是否存在

        if (userService.lambdaQuery().eq(User::getNo, user.getNo()).list().isEmpty()){
            return Result.fail();
        }

        if (StringUtils.isBlank(user.getNo()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getConfirmPassword())){
            return Result.fail();
        }
        if (!user.getPassword().equals(user.getConfirmPassword())){
            return Result.fail();
        }
        if (!userService.lambdaQuery().eq(User::getNo, user.getNo()).list().isEmpty()){
            return Result.fail();
        }
        user.setPassword(user.getPassword());
        return userService.save(user)? Result.success(user) : Result.fail();
    }
}
