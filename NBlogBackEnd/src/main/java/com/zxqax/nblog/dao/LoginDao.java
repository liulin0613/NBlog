package com.zxqax.nblog.dao;

import com.zxqax.nblog.dto.UserDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDao {

    /**
     * 根据用户名获取密码
     * @param name  用户名
     * @return 用户密码，通过 sha 256 加密算法加密
     */
    String getPwdByUserName(String name);

    /**
     * 根据邮箱获取密码
     * @param name  邮箱
     * @return 用户密码，通过 sha 256 加密算法加密
     */
    String getPwdByEmail(String name);

    /**
     * 通过邮箱获取用户名
     * @param name 邮箱
     * @return 用户名
     */
    String getNameByEmail(String name);

    /**
     * 根据邮箱获取密码 + 用户名
     * @param name  邮箱
     * @return 用户密码，用户名
     */
    UserDTO getPwdAndNameByEmail(String name);
}
