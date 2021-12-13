package com.zxqax.nblog.dto;

import lombok.Data;

@Data
public class PrintDTO {
    /**
     * 作者
     */
    private String author;

    /**
     * 标签
     */
    private String tags;

    /**
     * 文件夹
     */
    private String dir;
}
