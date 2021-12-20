package com.nblog.dto;

import lombok.Data;

@Data
public class DraftDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 更新时间
     */
    private String updateTime;
}
