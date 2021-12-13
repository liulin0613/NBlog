package com.zxqax.nblog.dto;

import lombok.Data;

@Data
public class FavoriteDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 更新时间
     */
    private String updateTime;
}
