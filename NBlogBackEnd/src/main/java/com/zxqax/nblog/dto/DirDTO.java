package com.zxqax.nblog.dto;

import lombok.Data;

@Data
public class DirDTO {
    /**
     * 文件夹 id
     */
    private Integer id;

    /**
     * 当前文件夹下的文章数量
     */
    private Integer count;

    /**
     * 文件夹名称
     */
    private String name;

}
