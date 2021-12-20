package com.nblog.service;

import com.nblog.dto.Result;

/**
 * 图片服务
 * @author liulin
 */
public interface ImageService {
    /**
     * 图片上传
     * @param file 图片
     * @return Result
     */
    Result<String> upload(String file);

    /**
     * 图片删除
     * @param path 路径
     * @return result
     */
    Result<Boolean> del(String path);

    /**
     * 修改头像
     * @param img
     * @return
     */
    Result<String> updateAvatar(String img);
}
