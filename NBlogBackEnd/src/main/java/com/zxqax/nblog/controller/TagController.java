package com.zxqax.nblog.controller;

import com.zxqax.nblog.dto.ArticleDTO;
import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.dto.TagArticleDTO;
import com.zxqax.nblog.dto.TagDTO;
import com.zxqax.nblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 与标签有关
 * @author liulin
 */
@RestController
@RequestMapping("/tag")
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
    @PostMapping("/getHotTags")
    @CrossOrigin
    public Result<List<TagDTO>> getHotTags(){
        return tagService.getHotTags();
    }

    /**
     * 获取所有标签
     * @return tag
     */
    @PostMapping("/getAllTags")
    @CrossOrigin
    public Result<List<TagDTO>> getAllTags(){
        return tagService.getAllTags();
    }

    /**
     * 获取 标签下的所有文章
     * @param tagname 标签
     * @return Msg
     */
    @PostMapping("/getTagArticleData")
    @CrossOrigin
    public Result<List<TagArticleDTO>> getTagArticleData(@RequestParam(value = "tagname") String tagname){
        return tagService.getTagArticleData(tagname);
    }
}
