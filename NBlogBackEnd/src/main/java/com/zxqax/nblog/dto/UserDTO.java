package com.zxqax.nblog.dto;

import lombok.Data;

@Data
public class UserDTO {
    /**
     * 用户 id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String uName;

    /**
     * 密码
     */
    private  String uPwd;

    /**
     * 头像
     */
    private String avatar;
}
