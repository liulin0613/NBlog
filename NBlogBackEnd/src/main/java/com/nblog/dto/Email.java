package com.nblog.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "email")
public class Email {
    /**
     * 发送邮箱服务器
     */
    private String host;

    /**
     * 发送端口
     */
    private String port;

    /**
     * 是否开启权限控制
     */
    private String auth;

    /**
     * 发送的协议是简单的邮件传输协议
     */
    private String protocol;

    /**
     * 开启 ssl
     */
    private String enable;

    /**
     * 发送者地址
     */
    private String fromAddress;

    /**
     * 授权密码
     */
    private String password;
}
