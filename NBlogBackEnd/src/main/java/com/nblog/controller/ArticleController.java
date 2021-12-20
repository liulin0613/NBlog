package com.nblog.controller;

import com.nblog.annotation.LogAnnotation;
import com.nblog.annotation.PassToken;
import com.nblog.dto.*;
import com.nblog.entity.Draft;
import com.nblog.service.ArticleService;
import com.nblog.vo.ArticleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.nblog.constant.OptTypeConst.*;

/**
 * @author liulin
 * 处理文章相关
 */

@RestController
@RequestMapping("/article")
@Api(tags = "文章模块")
public class ArticleController {
    private ArticleService articleService;

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 首页展示 分页获取文章
     * @param page 页数
     * @param limit 每页的条数
     * @return Result 包含文章列表信息
     */
    @GetMapping("/getArticlesByPagination")
    @ApiOperation(value = "分页获取文章")
    @PassToken
    public Result<ArticlePageDTO> getArticlesByPagination(@RequestParam(value = "page") int page,
                                                          @RequestParam(value = "limit") int limit){

        return articleService.getArticlesByPagination(page,limit);
    }

    /**
     * 获取热门文章
     * @return
     */
    @GetMapping("/getRecommendArticle")
    @ApiOperation(value = "获取热门文章")
    @PassToken
    public Result<List<DirDTO>> getRecommendArticle(){
        return articleService.getRecommendArticle();
    }

    /**
     * 根据 文章 ID 获取文章详情
     * @param id 文章 ID
     * @return 一篇文章的详情
     */
    @GetMapping("/getArticleDetailsByID")
    @ApiOperation(value = "查看文章")
    @LogAnnotation(optType = SELECT)
    @PassToken
    public Result<ArticleDetailsDTO> getArticleDetailsByID(@RequestParam(value = "id") int id){
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
    public Result<Boolean> addDraft(ArticleVO articleVO){
        return articleService.addDraft(articleVO);
    }

    /**
     * 发表文章
     * @param articleVO 文章
     * @return
     */
    @PostMapping("/addArticle")
    @ApiOperation(value = "发表文章")
    @LogAnnotation(optType = SAVE)
    public Result<Boolean> addArticle(ArticleVO articleVO){
        return articleService.addArticle(articleVO);
    }

    /**
     * 删除文章
     * @return
     */
    @DeleteMapping("/deleteArticle")
    @ApiOperation(value = "删除文章")
    @LogAnnotation(optType = REMOVE)
    public Result<Boolean>  deleteArticle(@RequestParam(value = "aid")   int aid){
        return articleService.deleteArticle(aid);
    }

    /**
     * 获取打印的信息
     * @param aid
     * @return
     */
    @GetMapping("/getPrintDetails")
    @ApiOperation(value = "打印的信息")
    public Result<PrintDTO>  getPrintDetails(@RequestParam(value = "aid") int aid){
        return articleService.getPrintDetails(aid);
    }

    /**
     * 获取重新编辑的文章
     * @param aid
     * @return
     */
    @GetMapping("/getRePublishArticle")
    @ApiOperation(value = "获取重新编辑的文章")
    public Result<ArticleDetailsDTO>  getRePublishArticle(@RequestParam(value = "aid") int aid){
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
    public Result<Boolean> reEditorSubmit(ArticleVO articleVO){
        return articleService.reEditorSubmit(articleVO);
    }

    /**
     * 获取所有草稿
     * @return
     */
    @GetMapping("/getAllDraft")
    @ApiOperation(value = "获取所有草稿")
    public Result<List<DraftDTO>> getAllDraft(){
        return articleService.getAllDraft();
    }

    /**
     * 删除草稿
     * @return
     */
    @DeleteMapping("/deleteDraft")
    @ApiOperation(value = "删除草稿")
    @LogAnnotation(optType = REMOVE)
    public Result<Boolean> deleteDraft(@RequestParam(value = "did")  int did){
        return articleService.deleteDraft(did);
    }

    /**
     * 获取草稿信息
     * @return
     */
    @GetMapping("/getReDraftInfo")
    @ApiOperation(value = "获取草稿信息")
    public Result<Draft> getReDraftInfo(@RequestParam(value = "did")  int did){
        return articleService.getReDraftInfo(did);
    }

    /**
     * 获取发表文章的图表数据
     * @return
     */
    @GetMapping("/getEchartsData")
    @ApiOperation(value = "获取发表文章的图表数据")
    public Result<Map<String,List<Integer>>> getEchartsData(){
        return articleService.getEchartsData();
    }

    /**
     * 根据 用户 ID 获取发表的所有非私密文章
     * @param user
     * @return
     */
    @GetMapping("/getPublishedByUser")
    @ApiOperation(value = "根据用户 ID 获取发表的所有文章")
    public Result<List<ArticleDTO>> getPublishedByUser(@RequestParam(value = "user") int user){
        return articleService.getPublishedByUser(user);
    }
}
