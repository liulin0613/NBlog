package com.zxqax.nblog.service;

import com.zxqax.nblog.dto.ArticleAboutUserDTO;
import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.entity.UserInfo;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 */

public interface UserService {

    /**
     *  检查用户名是否存在
     *  @param name 传入用户名
     *  @return 是否存在
     */
    Result<?> checkIfTheUserNameExists(String name);

    /**
     * 检查邮箱是否存在
     * @param email 用户邮箱
     * @return 是否存在
     */
    Result<?> checkIfTheEmailExists(String email);

    /**
     * 发送邮箱验证码
     * @param email 传入用户邮箱
     * @return Result 信息 是否发送成功
     */
    Result<?> sendEmailVerificationCode(String email);

    /**
     * 判断用户 ${current_user_id} 是否收藏了博文 ${id}
     * @param current_user_id  name 用户
     * @param id 博文 id
     * @return isFavorites true or false
     */
    int getIs_Favorites(int current_user_id, int id);

    /**
     * 判断用户 ${current_user_id} 是否关注了用户 ${author_id}
     * @param current_user_id name id
     * @param author_id fname id
     * @return 是否关注  0 or 1
     */
    int getIs_Attention(int current_user_id, int author_id);

    /**
     * 获取当前用户登录信息
     * @return UserInfo
     */
    UserInfo getCurrentUser();

    /**
     * 保存用户登录信息
     * @param token token
     * @param userInfo 用户信息
     */
    void saveUser(String token,UserInfo userInfo);


    /**
     * 下线用户
     * @param token token
     */
    void removeUser(String token);

    /**
     * 退出登录
     */
    void exit();

    /**
     * 添加关注，用户 ${name} 关注用户 ${fname}
     * @param fid 被关注用户
     * @return addFollow true or false
     */
    Result<?> addFollow(int fid);

    /**
     * 删除关注，用户 ${name} 删除关注用户 ${fname}
     * @param fid 被关注用户
     * @return addFollow true or false
     */
    Result<?> deleteFollow(int fid);

    /**
     * 用户 ${name} 添加 博文收藏 ${aid}
     * @param aid 博文 id
     * @return  addFavorites true or false
     */
    Result<?> addFavorites(int aid);

    /**
     * 用户 ${name} 删除 博文收藏 ${aid}
     * @param aid 博文 id
     * @return  deleteFavorites true or false
     */
    Result<?> deleteFavorites(int aid);

    /**
     * 用户 ${name} 添加 博文收藏 ${aid}
     * @param aid 博文 id
     * @return  addFavorites true or false
     */
    Result<?> addLike(int aid);

    /**
     * 用户 ${name} 删除 博文收藏 ${aid}
     * @param aid 博文 id
     * @return  deleteFavorites true or false
     */
    Result<?> deleteLike(int aid);

    /**
     * 检查用户的登录状态是否过期
     * @return
     */
    Result<?> checkExpiration();

    /**
     * 文章的信息统计
     * @return
     */
    Result<?> getArticleInfo();

    /**
     * 获取社交信息
     * @return
     */
    Result<?> getSocialInfo();

    /**
     * 修改图片
     * @param id
     * @param s
     */
    void updateImg(int id, String s);

    /**
     * 修改用户名
     * @param name
     * @return
     */
    Result<?> updateUserName(String name);

    /**
     * 修改用户简介
     * @param intro
     * @return
     */
    Result<?> updateUserIntro(String intro);

    /**
     * 修改用户密码
     * @param pwd
     * @return
     */
    Result<?> updateUserPwd(String pwd);

    /**
     * 获取所有关注的用户信息
     * @return
     */
    Result<?> getAllFollow();

    /**
     *
     * 获取发布详情
     * @param id
     * @return
     */
    Result<?> getPublishedByID(int id);

    /**
     * 访问量统计
     * @return
     */
    Result<?> addViews();

    /**
     * 获取访问量
     * @return
     */
    Result<?> getViews();
}
