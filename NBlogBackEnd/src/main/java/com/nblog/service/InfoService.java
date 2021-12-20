package com.nblog.service;

import com.nblog.dto.Result;
import com.nblog.dto.SocialInfoDTO;

public interface InfoService {
    /**
     * 修改用户名
     * @param name
     * @return
     */
    Result<String> updateUserName(String name);

    /**
     * 修改用户简介
     * @return
     */
    Result<Boolean> updateUserIntro(String intro);

    /**
     * 修改用户密码
     * @param pwd
     * @return
     */
    Result<Boolean> updateUserPwd(String pwd);

    /**
     * 检查用户的登录状态是否过期
     * @return
     */
    Result<Boolean> checkExpiration();

    /**
     * 获取社交信息
     * @return
     */
    Result<SocialInfoDTO> getSocialInfo();
}
