package com.zxqax.nblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LogDTO {
    /**
     * 请求 ip
     */
    private String ip;

    /**
     * 请求地区
     */
    private String address;

    /**
     * 请求参数
     */
    private String args;

    /**
     * 响应方法
     */
    private String responseMethod;

    /**
     * 返回结果
     */
    private Object result;
}
