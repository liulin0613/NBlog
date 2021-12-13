package com.zxqax.nblog.service;

import com.zxqax.nblog.dto.Result;

/**
 * 注册服务
 */
public interface RegisterService {

    /**
     * userRegistration 用户注册
     * @param name 用户名
     * @param pwd 用户密码，通过 sha 256 加密算法加密
     * @param email 用户注册邮箱
     * @return 是否注册成功
     */
    Result<?> userRegistration(String name, String pwd, String email);
}
