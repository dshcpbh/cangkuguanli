package com.example.config;

import lombok.Data;

import java.util.HashMap;

/**
 * @author dsh
 */
@Data
public class QueryPage {

    private static int PAGE_SIZE = 10;
    private static int PAGE_NUM = 1;

    private int pageSize=PAGE_SIZE;
    private int pageNum=PAGE_NUM;

    private HashMap<String,Object> param=new HashMap<>();

}
