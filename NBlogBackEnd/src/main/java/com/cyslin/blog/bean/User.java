package com.cyslin.blog.bean;

import lombok.Data;

@Data
public class User {
    private String name;
    private  String time;
    private int followcount;
    private int fanscount;
    private int articlecount;
}
