package com.zxqax.nblog.dao;

import com.zxqax.nblog.dto.*;
import com.zxqax.nblog.entity.Article;
import com.zxqax.nblog.entity.Draft;
import com.zxqax.nblog.vo.ArticlePageVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDao {

    int getTotalArticleCount();

    List<ArticlePageVO> getArticlesByPagination(@Param(value = "page") int page,
                                                @Param(value = "limit") int limit);

    ArticleDetailsDTO getArticleDetailsByID(int id);

    /**
     * 获取所有公开文章
     * @return
     */
    List<ArticleDTO> getAllArticle();

    /**
     * 获取与用户交互相关的信息
     * @param current_user_id user id
     * @param id article id
     * @return
     */
    ArticleAboutUserDTO getOthers(@Param(value = "user") int current_user_id,
                                  @Param(value = "author") int author_id,
                                  @Param(value = "aid") int id);

    /**
     * 增加阅读量
     * @param current_user_id
     * @param id
     */
    void addview(@Param(value = "user") int current_user_id,
                 @Param(value = "aid") int id);

    /**
     * 添加草稿
     * @param draft
     */
    void addDraft(Draft draft);

    /**
     * 发表文章
     * @param article
     */
    void addArticle(Article article);

    /**
     * 更新文件路径
     * @param aid
     * @param path
     */
    void updateArticlePath(@Param("aid") int aid, @Param("path")  String path);

    /**
     * 获取收藏的文章
     * @return
     */
    List<FavoriteDTO> getAllFavorites(int uid);

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
     * 更新图片
     * @param id
     * @param articleImg
     */
    void updateImg(@Param("id") int id, @Param("img") String articleImg);

    /**
     * 删除文章
     * @param aid
     */
    void deleteArticle(int aid);

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
     * 打印信息
     * @param aid
     * @return
     */
    PrintDTO getPrintDetails(int aid);

    /**
     * 获取文章详情
     * @param aid
     * @return
     */
    ArticleDetailsDTO getRePublishArticle(int aid);

    /**
     * 重新编辑
     * @param article
     */
    void reEditorSubmit(Article article);

    /**
     * 获取推荐文章列表
     * @return
     */
    List<DirDTO> getRecommendArticle();

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

    /**
     * 获取所有的草稿
     * @param user
     * @return
     */
    List<DraftDTO> getAllDraft(int user);

    /**
     * 删除草稿
     * @param did
     */
    void deleteDraft(int did);

    /**
     * 获取草稿信息
     * @param aid
     * @return
     */
    Draft getReDraftInfo(int aid);

    /**
     * @param user
     * @param month
     * @return
     */
    int getEchartsDataByMonth(@Param("user") int user, @Param("month") String month);

    /**
     * @param user
     * @param s
     * @return
     */
    int getEchartsDataByYear(@Param("user")  int user, @Param("year") String s);
}
