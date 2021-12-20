package com.nblog.controller;

import com.nblog.annotation.PassToken;
import com.nblog.dto.Result;
import com.nblog.dto.TagArticleDTO;
import com.nblog.dto.TagDTO;
import com.nblog.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 与标签有关
 * @author liulin
 */
@RestController
@RequestMapping("/tag")
@Api(tags = "标签模块")
public class TagController {
    /**
     * Spring IOC 自动注入 TagService 实例
     * 使用 Setter的方式注入，更加具有可变性
     */
    private TagService tagService;

    @Autowired
    public void setService(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * 获取热点标签
     * @return tag
     */
    @GetMapping("/getHotTags")
    @ApiOperation(value = "获取热点标签")
    @PassToken
    public Result<List<TagDTO>> getHotTags(){
        return tagService.getHotTags();
    }

    /**
     * 获取所有标签
     * @return tag
     */
    @GetMapping("/getAllTags")
    @ApiOperation(value = "获取所有标签")
    @PassToken
    public Result<List<TagDTO>> getAllTags(){
        return tagService.getAllTags();
    }

    /**
     * 获取 标签下的所有文章
     * @param tag 标签
     * @return Msg
     */
    @GetMapping("/getTagArticleData")
    @ApiOperation(value = "获取标签下的所有文章")
    @PassToken
    public Result<List<TagArticleDTO>> getTagArticleData(@RequestParam(value = "tag") String tag){
        return tagService.getTagArticleData(tag);
    }
}
