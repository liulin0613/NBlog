package com.nblog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchStrategyEnum {

    /**
     * 默认索引
     */
    NBLOG(0,"nblog"),

    /**
     * 作者
     */
    AUTHOR(1,"author"),

    /**
     * 标题
     */
    TITLE(2,"title"),

    /**
     * 内容
     */
    CONTENT(3,"content"),

    /**
     * 混合搜索
     */
    MIXED(4,"mixed"),

    /**
     * 作用域
     */
    SCOPE(5,"scope");

    /**
     * 策略
     */
    private final Integer status;

    /**
     * 描述
     */
    private final String desc;
}
