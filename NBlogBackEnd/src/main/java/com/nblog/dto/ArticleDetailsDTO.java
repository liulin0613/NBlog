package com.nblog.dto;

import lombok.Data;

/**
 * 文章详情
 */
@Data
public class ArticleDetailsDTO {
    /**
     * 文章 id
     */
    private Integer id;

    /**
     * 作者 id
     */
    private Integer authorId;

    /**
     * 作者名字
     */
    private String name;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签
     */
    private String tags;

    /**
     * 是否收藏
     */
    private Boolean is_Collect;

    /**
     * 是否关注作者
     */
    private Boolean is_Attention;

    /**
     * 是否点赞
     */
    private Boolean is_like;

    /**
     * 总访问量
     */
    private Integer allView;

    /**
     * 总点赞量
     */
    private Integer allLike;

    /**
     * 总收藏量
     */
    private Integer allCollect;

    /**
     * 发表时间
     */
    private String  createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 访问域
     */
    private Integer scope;
}
