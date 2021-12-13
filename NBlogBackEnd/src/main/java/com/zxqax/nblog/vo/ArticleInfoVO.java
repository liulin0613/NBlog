package com.zxqax.nblog.vo;

import lombok.Data;

@Data
public class ArticleInfoVO {
    /**
     * 共发表的文章
     */
    private int count;

    /**
     * 访问最高的文章ID
     */
    private int aid;

    /**
     * 访问最高的文章标题
     */
    private String title;

    /**
     * 文章总数
     */
    private int total;
}
