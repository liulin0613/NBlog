package com.nblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 文章搜索的内容
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleDTO {
    /**
     * id
     */
    private String id;

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
     * 创建时间
     */
    private String createTime;
}
