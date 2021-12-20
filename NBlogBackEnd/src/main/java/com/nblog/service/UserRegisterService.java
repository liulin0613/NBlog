package com.nblog.service;

import com.nblog.dto.Result;

/**
 * 用户注册服务
 * @author liulin
 */

public interface UserRegisterService {
    /**
     * 检查用户名是否存在
     * @param name
     * @return
     */
    Result<Integer> checkIfTheUserNameExists(String name);

    /**
     * 检查邮箱是否存在
     * @param email
     * @return
     */
    Result<Integer> checkIfTheEmailExists(String email);

    /**
     * 用户注册
     * @param name
     * @param pwd
     * @param email
     * @return
     */
    Result<String> userRegistration(String name, String pwd, String email);
}
