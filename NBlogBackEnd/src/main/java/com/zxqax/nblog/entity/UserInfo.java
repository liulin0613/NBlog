package com.zxqax.nblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户信息
 * @author liulin
 *
 * 对应 ： tb_user_info
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    /**
     * 用户 ID
     */
    private Integer id;

    /**
     * 用户 昵称
     */
    private String name;

    /**
     * 用户 密码
     */
    private String pwd;

    /**
     * 用户 邮箱
     */
    private String email;

    /**
     * 用户 头像
     */
    private String avatar;

    /**
     * 用户 简介
     */
    private String intro;

    /**
     * 用户 是否禁用
     */
    private Integer isDisable;

    /**
     * 用户 创建时间
     */
    private String createTime;

    /**
     * 用户 修改时间
     */
    private String updateTime;

}
