package com.nblog.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ArticleVO {
    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章范围
     */
    private String scope;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章标签
     */
    private String tags;

    /**
     * 重新编辑的文章目录
     */
    private String desc;

    /**
     * 重新编辑的文章 id
     */
    private Integer aid;
}
