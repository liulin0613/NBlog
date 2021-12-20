package com.nblog.service;

import com.nblog.dto.Result;

/**
 * 用户登录服务
 * @author liulin
 */

public interface UserLoginService {
    /**
     * 用户登录
     * @param name
     * @param pwd
     * @return
     */
    Result<?> loginViaUserNameAndPassword(String name, String pwd);

    /**
     * 邮箱登录
     * @param email
     * @return
     */
    Result<String> sendEmailCode(String email);

    /**
     * 邮箱登录验证
     * @param uuid
     * @param code
     * @return
     */
    Result<?> emailLoginVerification(String uuid,String code);

    /**
     * 退出登录
     * @return
     */
    Result<Boolean> loginOut();
}
