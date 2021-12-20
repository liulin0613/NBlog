package com.nblog.dto;

import lombok.Data;

import java.util.List;

@Data
public class SocialInfoDTO {
    /**
     * 所在地
     */
    private String location;

    /**
     * 关注人数
     */
    private int attCount;

    /**
     * 关注
     */
    private List<UserDTO> att;

    /**
     * 粉丝人数
     */
    private int fansCount;

    /**
     * 粉丝
     */
    private List<UserDTO> fans;
}
