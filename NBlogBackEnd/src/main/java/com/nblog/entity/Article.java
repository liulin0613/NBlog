package com.nblog.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章信息
 * @author liulin
 *
 * 对应 ： tb_article
 */

@Data
public class Article {
    /**
     * id
     */
    private Integer id;

    /**
     * 作者
     */
    private Integer userId;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 内容
     */
    private String articleContent;

    /**
     * 文章描述
     */
    private String articleDesc;

    /**
     * 文章标签
     */
    private String articleTags;

    /**
     * 文章所在文件夹
     */
    private Integer dir;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 文章状态 1.公开 2.私密 3.粉丝可见
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 展示图片
     */
    private String img;
}
