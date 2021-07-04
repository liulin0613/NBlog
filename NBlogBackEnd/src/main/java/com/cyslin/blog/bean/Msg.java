package com.cyslin.blog.bean;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Msg {
    private int code; //状态码 100 -- 成功，200 -- 失败
    private String msg;//提示信息
    private Map<String,Object> extend=new HashMap<>(); //用户返回的信息

    //成功
    public static Msg success(){
        Msg result=new Msg();
        result.setCode(100);
        result.setMsg("处理成功！");
        return result;
    }

    //失败
    public static Msg fail(){
        Msg result=new Msg();
        result.setCode(200);
        result.setMsg("处理失败！");
        return result;
    }

    //链式操作
    public Msg add(String key,Object value){
        this.extend.put(key,value);
        return this;
    }
}
