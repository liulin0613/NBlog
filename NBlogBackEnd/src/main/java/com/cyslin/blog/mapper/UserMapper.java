package com.cyslin.blog.mapper;

import com.cyslin.blog.bean.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int Checkname(String name);

    void addUser(@Param("name") String name,@Param("pwd") String pwd,@Param("email")  String email,@Param("time")  String time);

    String getPwdByName(String name);

    int Checkemail(String email);

    String getNameByEmail(String email);

    int getuIdByName(String name);

    void addfollow(@Param("uid") int uid,@Param("fid") int fid);

    int hasfollow(@Param("uid") int uid,@Param("fid")  int fid);

    void deletefollow(int uid, int fid);

    List<Integer> getAllfollows(int uid);

    String getNameByID(Integer follow);

    String getTimeByID(Integer follow);

    int getfollowcount(Integer follow);

    int getfanscount(Integer follow);

    int getarticlecount(String uname);

    List<Article> getPublishedByName(String name);

    String getEmailbyName(String name);

    int hasCard(String name);

    String getAlertTime(String name);

    void alertUserName(@Param("oldname") String oldname,@Param("name")  String name);

    void alertArticleName(@Param("oldname")String oldname, @Param("name")String name);

    void alertDraftName(@Param("oldname")String oldname, @Param("name")String name);

    void addAlertHistory(@Param("oldname")String oldname,  @Param("name")String name, @Param("time")String time);

    void alertPwd(@Param("name")String name, @Param("pwd")String pwd);
}
