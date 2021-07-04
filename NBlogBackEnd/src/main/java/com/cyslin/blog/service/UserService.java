package com.cyslin.blog.service;

import com.cyslin.blog.bean.Msg;

public interface UserService {

    Msg Checkname(String name);

    Msg emailYzm(String email);

    Msg addUser(String name, String pwd, String email);

    Msg userLogin(String name, String pwd);

    Msg emailLogin(String email);

    Msg emailExist(String email);

    Msg addfollow(String name, String fname);

    Msg hasfollow(String name, String fname);

    Msg deletefollow(String name, String fname);

    Msg getAllfollow(String name);

    Msg getPublishedByName(String name);

    Msg getinfo(String name);

    Msg alertName(String oldname, String name);

    Msg alertPwd(String name, String pwd);
}

