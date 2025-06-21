package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dsh
* @description 针对表【record】的数据库操作Service
* @createDate 2025-06-06 17:40:23
*/
public interface RecordService extends IService<Record> {

    IPage<Record> pagec(Page<Record> page, QueryWrapper<Record> queryWrapper);
}
