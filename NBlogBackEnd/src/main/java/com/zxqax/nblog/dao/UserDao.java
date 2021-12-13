package com.zxqax.nblog.dao;

import com.zxqax.nblog.dto.ArticleDTO;
import com.zxqax.nblog.dto.AttDTO;
import com.zxqax.nblog.dto.UserDTO;
import com.zxqax.nblog.entity.UserInfo;
import com.zxqax.nblog.vo.ArticleInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    /**
     *  检查用户名是否存在
     *  @param name 传入用户名
     *  @return 是否存在
     */
    int checkIfTheUserNameExists(String name);


    /**
     * 检查邮箱是否存在
     * @param email 用户邮箱
     * @return 是否存在
     */
    int checkIfTheEmailExists(String email);

    /**
     * 根据用户名获取用户信息
     * @param name 用户名
     * @return 用户信息
     */
    UserInfo getInfo(String name);

    /**
     * 判断用户 ${current_user_id} 是否收藏了博文 ${id}
     * @param current_user_id  name 用户
     * @param id 博文 id
     * @return isFavorites true or false
     */
    int getIs_Favorites(@Param(value = "uid") int current_user_id,
                        @Param(value = "aid")int id);

    /**
     * 判断用户 ${current_user_id} 是否关注了用户 ${author_id}
     * @param current_user_id name id
     * @param author_id fname id
     * @return 是否关注  0 or 1
     */
    int getIs_Attention(@Param(value = "uid") int current_user_id,
                        @Param(value = "fid") int author_id);

    /**
     * 关注用户
     * @param uid 用户
     * @param fid 被关注用户
     */
    void addFollow(@Param("uid") int uid,@Param("fid") int fid);

    /**
     * 删除关注，用户 ${name} 删除关注用户 ${fname}
     * @param  uid name 用户
     * @param fid fname 被关注用户
     */
    void deleteFollow(@Param("uid") int uid,@Param("fid")  int fid);

    /**
     * 用户 ${name} 添加 博文收藏 ${aid}
     * @param aid 博文 id
     * @param uid name 用户 ${name}
     */
    void addFavorites(@Param("aid") int aid, @Param("uid")  int uid);

    /**
     * 用户 ${name} 删除 博文收藏 ${aid}
     * @param aid 博文 id
     * @param uid name 用户 ${name}
     */
    void deleteFavorites(@Param("aid") int aid, @Param("uid") int uid);

    /**
     * 点赞
     * @param aid
     * @param current
     */
    void addLike(@Param("aid") int aid, @Param("uid") int current);

    /**
     * 删除 点赞
     * @param aid
     * @param current
     */
    void deleteLike(@Param("aid") int aid, @Param("uid")  int current);

    /**
     * 文章信息
     * @return
     */
    ArticleInfoVO ArticleInfo(int user);

    /**
     * 获取所有关注用户
     * @param user
     * @return
     */
    List<UserDTO> getAtts(int user);

    /**
     * 获取所有粉丝
     * @param user
     * @return
     */
    List<UserDTO> getFans(int user);

    /**
     * 更新图片
     * @param id
     * @param s
     */
    void updateImg(@Param("id") int id, @Param("img") String s);

    /**
     * 更新用户名
     * @param user
     * @param name
     */
    void updateUserName(@Param("user")int user,@Param("name") String name);

    /**
     *更新用户简介
     * @param user
     * @param intro
     */
    void updateUserIntro(@Param("user") int user, @Param("intro") String intro);

    /**
     * 更新用户密码
     * @param user
     * @param pwd
     */
    void updateUserPwd(@Param("user") int user, @Param("pwd") String pwd);

    /**
     * 获取关注信息
     * @param user
     * @return
     */
    List<AttDTO> getAllFollow(int user);

    /**
     * 发布博文信息
     * @param id
     * @return
     */
    List<ArticleDTO> getPublishedByID(int id);
}
