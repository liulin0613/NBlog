package com.zxqax.nblog.controller;

import com.zxqax.nblog.annotation.LogAnnotation;
import com.zxqax.nblog.dto.ArticlePageDTO;
import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.service.ArticleService;
import com.zxqax.nblog.vo.ArticleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.zxqax.nblog.constant.OptTypeConst.*;

/**
 * 与文章有关
 * @author liulin
 */

@RestController
@RequestMapping("/article")
@Api(tags = "文章模块")
public class ArticleController {
    /**
     * Spring IOC 自动注入 ArticleService 实例
     * 使用 Setter的方式注入，更加具有可变性
     */
    private ArticleService articleService;

    @Autowired
    public void setService(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 首页展示 分页获取文章
     * @param page 页数
     * @param limit 每页的条数
     * @return Result 包含文章列表信息
     */
    @PostMapping("/getArticlesByPagination")
    @ApiOperation(value = "分页获取文章")
    public Result<ArticlePageDTO> getArticlesByPagination(@RequestParam(value = "page") int page,
                                                          @RequestParam(value = "limit") int limit){

        return articleService.getArticlesByPagination(page,limit);
    }

    /**
     * 根据 文章 ID 获取文章详情
     * @param id 文章 ID
     * @return 一篇文章的详情
     */
    @PostMapping("/getArticleDetailsByID")
    @ApiOperation(value = "查看文章")
    @LogAnnotation(optType = SELECT)
    public Result<?> getArticleDetailsByID(int id){
        return articleService.getArticleDetailsByID(id);
    }

    /**
     * 保存文章到草稿
     * @param articleVO 文章
     * @return
     */
    @PostMapping("/addDraft")
    @ApiOperation(value = "添加草稿")
    @LogAnnotation(optType = SAVE_OR_UPDATE)
    public Result<?> addDraft(ArticleVO articleVO){
        return articleService.addDraft(articleVO);
    }

    /**
     * 保存文章
     * @param articleVO 文章
     * @return
     */
    @PostMapping("/addArticle")
    @ApiOperation(value = "添加文章")
    @LogAnnotation(optType = SAVE)
    public Result<?> addArticle(ArticleVO articleVO){
        return articleService.addArticle(articleVO);
    }

    /**
     * 获取用户所有收藏的文章
     * @return
     */
    @PostMapping("/getAllFavorites")
    public Result<?>  getAllFavorites(){
        return articleService.getAllFavorites();
    }

    /**
     * 获取用户所有未归档文章
     * @return
     */
    @PostMapping("/getNotArchived")
    public Result<?>  getNotArchived(){
        return articleService.getNotArchived();
    }

    /**
     * 获取用户所有文件夹
     * @return
     */
    @PostMapping("/getDirs")
    public Result<?>  getDirs(){
        return articleService.getDirs();
    }

    /**
     * 获取用户文件夹内的文章
     * @return
     */
    @PostMapping("/getDirsDetails")
    public Result<?>  getDirsDetails(int aid){
        return articleService.getDirsDetails(aid);
    }

    /**
     * 根据内容和文件夹搜索文章
     * @return
     */
    @PostMapping("/searchByTitleAndDir")
    public Result<?>  searchByTitleAndDir(String title,int did){
        return articleService.searchByTitleAndDir(title,did);
    }


    /**
     * 删除文章
     * @return
     */
    @PostMapping("/deleteArticle")
    @ApiOperation(value = "删除文章")
    @LogAnnotation(optType = REMOVE)
    public Result<?>  deleteArticle(int aid){
        return articleService.deleteArticle(aid);
    }

    /**
     * 创建文件夹
     * @return
     */
    @PostMapping("/addDir")
    @ApiOperation(value = "创建文件夹")
    @LogAnnotation(optType = SAVE)
    public Result<?>  addDir(String dirName){
        return articleService.addDir(dirName);
    }

    /**
     * 将 aid 移动到 did
     * @param aid
     * @param did
     * @return
     */
    @PostMapping("/removeArticle")
    public Result<?>  removeArticle(int aid,int did){
        return articleService.removeArticle(aid,did);
    }

    /**
     * 打印的信息
     * @param aid
     * @return
     */
    @PostMapping("/printDetails")
    public Result<?>  getPrintDetails(int aid){
        return articleService.getPrintDetails(aid);
    }


    /**
     * 获取重新编辑的文章
     * @param aid
     * @return
     */
    @PostMapping("/getRePublishArticle")
    public Result<?>  getRePublishArticle(int aid){
        return articleService.getRePublishArticle(aid);
    }

    /**
     * 保存重新编辑的文章
     * @param articleVO 文章
     * @return
     */
    @PostMapping("/reEditorSubmit")
    @ApiOperation(value = "保存重新编辑的文章")
    @LogAnnotation(optType = SAVE)
    public Result<?> reEditorSubmit(ArticleVO articleVO){
        return articleService.reEditorSubmit(articleVO);
    }

    /**
     * 获取热门文章
     * @return
     */
    @PostMapping("/getRecommendArticle")
    public Result<?> getRecommendArticle(){
        return articleService.getRecommendArticle();
    }

    /**
     * 删除文件夹
     * @return
     */
    @PostMapping("/deleteDir")
    @ApiOperation(value = "删除文件夹")
    @LogAnnotation(optType = REMOVE)
    public Result<?> deleteDir(int did){
        return articleService.deleteDir(did);
    }

    /**
     * 获取所有草稿
     * @return
     */
    @PostMapping("/getAllDraft")
    public Result<?> getAllDraft(){
        return articleService.getAllDraft();
    }

    /**
     * 删除草稿
     * @return
     */
    @PostMapping("/deleteDraft")
    @ApiOperation(value = "删除草稿")
    @LogAnnotation(optType = REMOVE)
    public Result<?> deleteDraft(int did){
        return articleService.deleteDraft(did);
    }

    /**
     * 草稿信息
     * @return
     */
    @PostMapping("/getReDraftInfo")
    public Result<?> getReDraftInfo(int aid){
        return articleService.getReDraftInfo(aid);
    }

    /**
     * 获取发表文章的图表数据
     * @return
     */
    @PostMapping("/getEchartsData")
    public Result<?> getEchartsData(){
        return articleService.getEchartsData();
    }
}
