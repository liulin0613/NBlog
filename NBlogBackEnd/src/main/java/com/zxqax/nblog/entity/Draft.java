package com.zxqax.nblog.entity;

import lombok.Data;
import lombok.ToString;

/**
 * 草稿信息
 *
 * @author liulin
 *
 * 对应 ： tb_draft
 */

@Data
@ToString
public class Draft {
    /**
     * id
     */
    private Integer id;

    /**
     * user_id
     */
    private Integer userId;

    /**
     * title
     */
    private String title;

    /**
     * content
     */
    private String content;

    /**
     * tags
     */
    private String tags;

    /**
     * scope 范围 公开 1 ，私密 2，粉丝可见 3
     */
    private Integer scope;

    /**
     * createTime
     */
    private String createTime;

    /**
     * updateTime
     */
    private String updateTime;

    /**
     * status 状态 0 表示删除
     */
    private Integer status;

}
