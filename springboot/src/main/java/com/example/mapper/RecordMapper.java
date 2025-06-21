package com.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author dsh
* @description 针对表【record】的数据库操作Mapper
* @createDate 2025-06-06 17:40:23
* @Entity com.example.entity.Record
*/
@Mapper
public interface RecordMapper extends BaseMapper<Record> {

    IPage<Record> pagec(@Param("page") IPage<Record> page, @Param(Constants.WRAPPER) QueryWrapper<Record> queryWrapper);
}




