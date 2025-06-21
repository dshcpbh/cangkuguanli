package com.example.mapper;

import com.example.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author dsh
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-06-01 08:38:49
* @Entity com.example.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




