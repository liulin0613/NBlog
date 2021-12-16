package com.zxqax.nblog.dto;

import lombok.Data;

@Data
public class AttDTO {
    /**
     * 用户 id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 注册时间
     */
    private String time;

    /**
     * 关注数量
     */
    private Integer attCount;

    /**
     * 粉丝数量
     */
    private Integer fansCount;

    /**
     * 发表文章数量
     */
    private Integer articleCount;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 简介
     */
    private String intro;
}
