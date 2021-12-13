package com.zxqax.nblog.dto;

import lombok.Data;

@Data
public class TagArticleDTO {
    /**
     * id
     */
    private String id;

    /**
     * 作者 id
     */
    private Integer userId;

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
    private String content;


    /**
     * 内容
     */
    private String tags;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 图片
     */
    private String img;
}
