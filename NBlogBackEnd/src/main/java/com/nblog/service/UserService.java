package com.nblog.service;

import com.nblog.dto.AttDTO;
import com.nblog.dto.FavoriteDTO;
import com.nblog.dto.Result;
import com.nblog.entity.UserInfo;

import java.util.List;

/**
 * 用户服务
 * @author liulin
 */

public interface UserService {
    /**
     * 保存用户登录信息
     * @param id
     * @param current_user
     */
    void saveUser(String id, UserInfo current_user);

    /**
     * 获取当前登录的用户的信息
     * @return
     */
    UserInfo getCurrentUser();

    /**
     * 用户 ${name} 添加 博文收藏 ${aid}
     * @param aid 博文 id
     * @return  addCollect true or false
     */
    Result<Boolean> addCollect(int aid);

    /**
     * 用户删除博文收藏 ${aid}
     * @param aid 博文 id
     * @return
     */
    Result<Boolean> deleteCollect(int aid);

    /**
     * 获取用户所有收藏的文章
     * @return
     */
    Result<List<FavoriteDTO>> getAllCollects();

    /**
     * 修改头像
     * @param id
     * @param s
     */
    void updateImg(int id, String s);

    /**
     * 添加关注，用户 ${name} 关注用户 ${fname}
     * @param fid 被关注用户
     * @return
     */
    Result<Boolean> addAttention(int fid);

    /**
     * 删除关注，用户 ${name} 删除关注用户 ${fname}
     * @param fid 被关注用户
     * @return
     */
    Result<Boolean> deleteAttention(int fid);

    /**
     * 获取所有关注的用户信息
     * @return
     */
    Result<List<AttDTO>> getAllAttention();

    /**
     * 用户 ${name} 点赞 博文 ${aid}
     * @param aid 博文 id
     * @return
     */
    Result<Boolean> addLike(int aid);

    /**
     * 用户 ${name}取消博文点赞 ${aid}
     * @param aid 博文 id
     * @return
     */
    Result<Boolean> deleteLike(int aid);

    /**
     * 添加首页访问量
     * @return
     */
    Result<Boolean> addViews();

    /**
     * 获取首页访问量
     * @return
     */
    Result<Integer> getViews();
}
