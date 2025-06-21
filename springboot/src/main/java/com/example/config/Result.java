package com.example.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dsh
 */
@Data
@Setter
@Getter
public class Result {
    private int code;
    private String msg;
    private Object data;
    private Long total;
    //成功返回结果
    public static Result success(){
        return Result.result(200,"成功",null,null);
    }
    public static Result success(Object data){
        return Result.result(200,"成功",data,null);
    }
    public static Result success(Object data,Long total){
        return Result.result(200,"成功",data,total);
    }
    //失败返回结果
    public static Result fail(){
        return Result.result(500,"失败",null,null);
    }


    public static Result result(int code, String msg, Object data, Long total){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        result.setTotal(total);
        return result;
    }

}
