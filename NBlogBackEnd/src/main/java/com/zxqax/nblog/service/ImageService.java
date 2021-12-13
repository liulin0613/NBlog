package com.zxqax.nblog.service;

import com.zxqax.nblog.dto.Result;

public interface ImageService {
    /**
     * 图片上传
     * @param file 图片
     * @return Result
     */
    Result<?> upload(String file);

    /**
     * 图片删除
     * @param path 路径
     * @return result
     */
    Result<?> del(String path);

    /**
     * 修改头像
     * @param img
     * @return
     */
    Result<?> alertAvatar(String img);
}
