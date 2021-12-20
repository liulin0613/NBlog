package com.nblog.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegisterDao {

    /**
     * 插入用户
     * @param name 用户名
     * @param pwd 密码
     * @param email 邮箱
     */
    void userRegistration(@Param(value = "name") String name,
                          @Param(value = "pwd") String pwd,
                          @Param(value = "email") String email);
}
