package com.nblog.dao;

import com.nblog.dto.ArticleDTO;
import com.nblog.dto.DirDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirDao {

    /**
     * 获取所有未归档文章
     * @param user
     * @return
     */
    List<ArticleDTO> getNotArchived(int user);

    /**
     * 获取文件夹
     * @param user
     * @return
     */
    List<DirDTO> getDirs(int user);

    /**
     * 获取用户文件夹内的文章
     * @param user
     * @param aid
     * @return
     */
    List<ArticleDTO> getDirsDetails(@Param("user")  int user,
                                    @Param("aid") int aid);

    /**
     * 通过标题搜索
     * @param user
     * @param title
     * @return
     */
    List<ArticleDTO> searchByTitle(@Param("user") int user,
                                   @Param("title") String title);

    /**
     * 通过标题搜索和文件夹
     * @param user
     * @param title
     * @return
     */
    List<ArticleDTO> searchByTitleAndDir(@Param("user") int user,
                                         @Param("title") String title,
                                         @Param("did") int did);

    /**
     * 增加文件夹
     * @param user
     * @param dirName
     */
    void addDir(@Param("user") int user,@Param("dirName")  String dirName);

    /**
     * 移动文章到指定文件夹
     * @param aid
     * @param did
     */
    void removeArticle(@Param("aid") int aid, @Param("did") int did);

    /**
     * 更新文件夹
     * @param did
     */
    void updateDir(int did);

    /**
     * 删除文件夹
     * @param did
     */
    void deleteDir(int did);
}
