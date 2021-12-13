package com.zxqax.nblog.dto;

import lombok.Data;

@Data
public class ArticleAboutUserDTO {
    /**
     * 是否收藏
     */
    private Integer isFavorites;

    /**
     * 是否关注作者
     */
    private Integer isAttention;

    /**
     * 是否点赞
     */
    private Integer isLike;
}
