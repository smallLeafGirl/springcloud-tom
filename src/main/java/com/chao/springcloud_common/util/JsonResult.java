package com.chao.springcloud_common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 7778889524286463327L;
    private Integer code;
    private String msg;
    private T data;

    private static Map<Integer,String> map = new HashMap<>();

    public static<T> JsonResult success(T data){
        return new JsonResult(0,map.get(0),data);
    }
    public static<T> JsonResult error(Integer code){
        return new JsonResult(code,map.get(code),null);
    }
    public static<T> JsonResult error(Integer code,T data){
        return new JsonResult(code,map.get(code),data);
    }
    public static<T> JsonResult custom(String msg){
        return new JsonResult(-3,msg,null);
    }
    public static<T> JsonResult custom(String msg,T data){
        return new JsonResult(-3,msg,data);
    }
    public static<T> JsonResult custom(Integer code,String msg,T data){
        return new JsonResult(code,map.get(code),data);
    }


    static {
        map.put(0,"OK");
        map.put(200,"请求有误");
        map.put(-1,"系统异常");
        map.put(-3,"自定义异常");
    }
}
