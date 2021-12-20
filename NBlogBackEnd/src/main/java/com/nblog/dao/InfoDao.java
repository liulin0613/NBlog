package com.nblog.dao;

import com.nblog.dto.UserDTO;
import com.nblog.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoDao {
    /**
     * 更新用户名
     * @param user
     * @param name
     */
    void updateUserName(@Param("user")int user, @Param("name") String name);

    /**
     *更新用户简介
     * @param user
     * @param intro
     */
    void updateUserIntro(@Param("user") int user, @Param("intro") String intro);

    /**
     * 更新用户密码
     * @param user
     * @param pwd
     */
    void updateUserPwd(@Param("user") int user, @Param("pwd") String pwd);

    /**
     * 获取社交信息
     * @param ids
     * @return
     */
    List<UserDTO> getSocialInfo(int[] ids);
}
