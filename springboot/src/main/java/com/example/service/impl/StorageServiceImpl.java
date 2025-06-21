package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Storage;
import com.example.service.StorageService;
import com.example.mapper.StorageMapper;
import org.springframework.stereotype.Service;

/**
* @author dsh
* @description 针对表【storage】的数据库操作Service实现
* @createDate 2025-06-05 17:05:03
*/
@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage>
    implements StorageService{

}




