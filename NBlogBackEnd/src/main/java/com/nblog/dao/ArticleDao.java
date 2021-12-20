package com.nblog.dao;

import com.nblog.dto.*;
import com.nblog.entity.Article;
import com.nblog.entity.Draft;
import com.nblog.vo.ArticlePageVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDao {

    /**
     * 获取文章总量
     * @return
     */
    int getTotalArticleCount();

    /**
     * 分页获取文章
     * @param page
     * @param limit
     * @return
     */
    List<ArticlePageVO> getArticlesByPagination(@Param(value = "page") int page,
                                                @Param(value = "limit") int limit);

    /**
     * 根据 ID 获取文章详情
     * @param id
     * @return
     */
    ArticleDetailsDTO getArticleDetailsByID(int id);

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
     * 删除文章
     * @param aid
     */
    void deleteArticle(int aid);

    /**
     * 根据id获取文章的 scope
     * @param aid
     * @return
     */
    int getScopeByID(int aid);

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
    List<DirDTO> getRecommendArticle(List<Integer> aids);

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
     * @param did
     * @return
     */
    Draft getReDraftInfo(int did);

    /**
     * 获取今年的发表数据
     * @param user
     * @param month
     * @return
     */
    int getEchartsDataByMonth(@Param("user") int user,
                              @Param("month") String month);

    /**
     * 获取近三年的发表数据
     * @param user
     * @param s
     * @return
     */
    int getEchartsDataByYear(@Param("user")  int user,
                             @Param("year") String s);

    /**
     * 获取用户发布的博文信息
     * @param user
     * @return
     */
    List<ArticleDTO> getPublishedByUser(int user);
}
