package com.nblog.dao;

import com.nblog.entity.UserInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginDao {

    /**
     * 根据邮箱获取用户信息
     * @param email
     * @return
     */
    UserInfo getInfoByEmail(String email);

    /**
     * 根据用户名获取用户信息
     * @param name
     * @return
     */
    UserInfo getInfoByName(String name);
}
