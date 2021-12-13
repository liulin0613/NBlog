package com.zxqax.nblog.service;

import com.zxqax.nblog.dto.Result;

/**
 * 登录服务
 * @author liulin
 * @date
 */
public interface LoginService {

    /**
     * 用户登录 登录方式 ：用户名 + 密码
     * @param name 用户名
     * @param pwd  用户密码 ，通过 sha 256 加密算法加密
     * @return 是否登录成功
     */
    Result<?> loginViaUserNameAndPassword(String name, String pwd);

    /**
     * loginViaEmail 用户登录 登录方式 ： 邮箱
     * @param email 用户邮箱
     * @return Msg 信息 其中 is_login 属性 表明是否登录成功
     */
    Result<?> loginViaEmail(String email);

    /**
     * 退出登录
     * @return
     */
    Result<?> exit();
}
