package com.nblog.vo;

import lombok.Data;


@Data
public class ArticlePageVO {
    /**
     * id
     */
    private int id;

    /**
     * 作者id
     */
    private String userId;

    /**
     * 作者
     */
    private String author;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String desc;

    /**
     * 标签
     */
    private String tags;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 图片
     */
    private String img;
}
