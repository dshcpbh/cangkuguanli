package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Record;
import com.example.service.RecordService;
import com.example.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author dsh
* @description 针对表【record】的数据库操作Service实现
* @createDate 2025-06-06 17:40:23
*/
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record>
    implements RecordService{

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public IPage<Record> pagec(Page<Record> page, QueryWrapper<Record> queryWrapper) {
        return recordMapper.pagec(page, queryWrapper);
    }
}




