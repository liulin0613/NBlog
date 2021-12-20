package com.nblog.service;

import com.nblog.dto.*;
import com.nblog.entity.Draft;
import com.nblog.vo.ArticleVO;

import java.util.List;
import java.util.Map;

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
    Result<ArticleDetailsDTO> getArticleDetailsByID(int id);

    /**
     * 添加文章到草稿
     * @param articleVO
     * @return
     */
    Result<Boolean> addDraft(ArticleVO articleVO);

    /**
     * 发表文章
     * @param articleVO
     * @return
     */
    Result<Boolean> addArticle(ArticleVO articleVO);

    /**
     * 删除文章
     * @param aid
     * @return
     */
    Result<Boolean> deleteArticle(int aid);

    /**
     * 打印的信息
     * @param aid
     * @return
     */
    Result<PrintDTO> getPrintDetails(int aid);

    /**
     * 获取文章详情
     * @param aid
     * @return
     */
    Result<ArticleDetailsDTO> getRePublishArticle(int aid);

    /**
     * 保存重新编辑的文章
     * @param articleVO
     * @return
     */
    Result<Boolean> reEditorSubmit(ArticleVO articleVO);

    /**
     * 获取推荐文章列表
     * @return
     */
    Result<List<DirDTO>> getRecommendArticle();

    /**
     * 获取所有的草稿
     * @return
     */
    Result<List<DraftDTO>> getAllDraft();

    /**
     * 删除草稿
     * @param did
     * @return
     */
    Result<Boolean> deleteDraft(int did);

    /**
     * 获取草稿信息
     * @param did
     * @return
     */
    Result<Draft> getReDraftInfo(int did);

    /**
     * 获取发表文章的图表数据
     * @return
     */
    Result<Map<String,List<Integer>>> getEchartsData();


    Result<List<ArticleDTO>> getPublishedByUser(int user);
}
