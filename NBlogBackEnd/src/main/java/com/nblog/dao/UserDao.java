package com.nblog.dao;

import com.nblog.dto.AttDTO;
import com.nblog.dto.FavoriteDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    /**
     * 邮箱是否存在
     * @param email
     * @return
     */
    int checkIfTheEmailExists(String email);

    /**
     * 检查用户名是否存在
     * @param name
     * @return
     */
    int checkIfTheUserNameExists(String name);

    /**
     * 根据 ID 获取用户名
     * @param userId
     * @return
     */
    String getNameByID(Integer userId);

    /**
     * 用户 ${name} 添加 博文收藏 ${aid}
     * @param aid 博文 id
     * @param uid name 用户 ${name}
     */
    void addCollect(@Param("aid") int aid, @Param("uid")  int uid);

    /**
     * 获取收藏的文章
     * @return
     */
    List<FavoriteDTO> getAllCollects(int[] ids);

    /**
     * 更新头像
     * @param id
     * @param s
     */
    void updateImg(@Param("id") int id, @Param("img") String s);

    /**
     * 获取关注信息
     * @param ids
     * @return
     */
    List<AttDTO> getAllAttention(int[] ids);
}
