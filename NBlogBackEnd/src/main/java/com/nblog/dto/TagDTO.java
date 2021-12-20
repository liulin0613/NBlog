package com.nblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签
 * @author liulin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    /**
     * 标签名
     */
    private String tagName;

    /**
     * 标签数量
     */
    private Integer tagCount;

}
