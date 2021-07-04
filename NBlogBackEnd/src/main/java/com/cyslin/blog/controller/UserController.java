package com.cyslin.blog.controller;

import com.cyslin.blog.bean.Article;
import com.cyslin.blog.bean.Msg;
import com.cyslin.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService service;

    @PostMapping("/user/Checkname")
    @CrossOrigin
    public Msg Checkname(String name){
        return service.Checkname(name);
    }

    @PostMapping("/user/emailYzm")
    @CrossOrigin
    public Msg emailYzm(String email){
        return service.emailYzm(email);
    }

    @PostMapping("/user/addUser")
    @CrossOrigin
    public Msg addUser(String name,String pwd,String email){
        return service.addUser(name,pwd,email);
    }

    @PostMapping("/user/userLogin")
    @CrossOrigin
    public Msg userLogin(String name,String pwd){
        return service.userLogin(name,pwd);
    }

    @PostMapping("/user/emailLogin")
    @CrossOrigin
    public Msg emailLogin(String email){
        return service.emailLogin(email);
    }

    @PostMapping("/user/emailExist")
    @CrossOrigin
    public Msg emailExist(String email){
        return service.emailExist(email);
    }

    @PostMapping("/user/addfollow")
    @CrossOrigin
    public Msg addfollow(String name,String fname){
        return service.addfollow(name,fname);
    }

    @PostMapping("/user/hasfollow")
    @CrossOrigin
    public Msg hasfollow(String name,String fname){
        return service.hasfollow(name,fname);
    }

    @PostMapping("/user/deletefollow")
    @CrossOrigin
    public Msg deletefollow(String name,String fname){
        return service.deletefollow(name,fname);
    }


    @PostMapping("/user/getAllfollow")
    @CrossOrigin
    public Msg getAllfollow(String name){
        return service.getAllfollow(name);
    }

    @PostMapping("/user/getPublishedByName")
    @CrossOrigin
    public Msg getPublishedByName(String name){
        return service.getPublishedByName(name);
    }

    @PostMapping("/user/getinfo")
    @CrossOrigin
    public Msg getinfo(String name){
        return service.getinfo(name);
    }

    @PostMapping("/user/alertName")
    @CrossOrigin
    public Msg alertName(String oldname,String name){
        return service.alertName(oldname,name);
    }

    @PostMapping("/user/alertPwd")
    @CrossOrigin
    public Msg alertPwd(String name,String pwd){
        return service.alertPwd(name,pwd);
    }
}
