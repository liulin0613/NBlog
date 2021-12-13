package com.zxqax.nblog.service;

import com.zxqax.nblog.dto.ArticlePageDTO;
import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.vo.ArticleVO;

public interface ArticleService {

    /**
     * 首页展示 分页获取文章
     * @param page 页数
     * @param limit 每页的条数
     * @return Result 包含文章列表信息
     */
    Result<ArticlePageDTO> getArticlesByPagination(int page, int limit);

    /**
     * 根据 文章 ID 获取文章详情
     * @param id 文章 ID
     * @return 一篇文章的详情
     */
    Result<?> getArticleDetailsByID(int id);


    Result<?> addDraft(ArticleVO articleVO);

    /**
     * 发表文章
     * @param articleVO
     * @return
     */
    Result<?> addArticle(ArticleVO articleVO);

    /**
     * 获取用户所有收藏的文章
     * @return
     */
    Result<?> getAllFavorites();

    /**
     * 获取未归档文章
     * @return
     */
    Result<?> getNotArchived();

    /**
     * 获取文件夹
     * @return
     */
    Result<?> getDirs();

    /**
     * 获取用户所有文件夹内的文章
     * @param aid
     * @return
     */
    Result<?> getDirsDetails(int aid);

    /**
     *根据内容和文件夹搜索文章
     * @param title
     * @param did
     * @return
     */
    Result<?> searchByTitleAndDir(String title, int did);

    /**
     * 删除文章
     * @param aid
     * @return
     */
    Result<?> deleteArticle(int aid);

    /**
     * 创建文件夹
     * @param dirName
     * @return
     */
    Result<?> addDir(String dirName);

    /**
     * 将 aid 移动到 did
     * @param aid
     * @param did
     * @return
     */
    Result<?> removeArticle(int aid, int did);

    /**
     * 打印的信息
     * @param aid
     * @return
     */
    Result<?> getPrintDetails(int aid);

    /**
     * 获取文章详情
     * @param aid
     * @return
     */
    Result<?> getRePublishArticle(int aid);

    /**
     * 保存重新编辑的文章
     * @param articleVO
     * @return
     */
    Result<?> reEditorSubmit(ArticleVO articleVO);

    /**
     * 获取推荐文章列表
     * @return
     */
    Result<?> getRecommendArticle();

    /**
     * 删除文件夹
     * @param did
     * @return
     */
    Result<?> deleteDir(int did);

    /**
     * 获取所有的草稿
     * @return
     */
    Result<?> getAllDraft();

    /**
     * 删除草稿
     * @param did
     * @return
     */
    Result<?> deleteDraft(int did);

    /**
     * 获取草稿信息
     * @param aid
     * @return
     */
    Result<?> getReDraftInfo(int aid);

    /**
     * 获取发表文章的图表数据
     * @return
     */
    Result<?> getEchartsData();
}
