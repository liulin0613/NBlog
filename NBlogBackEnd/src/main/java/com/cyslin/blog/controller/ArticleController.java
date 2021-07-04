package com.cyslin.blog.controller;

import com.cyslin.blog.bean.Article;
import com.cyslin.blog.bean.Msg;
import com.cyslin.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ArticleController {
    @Autowired
    ArticleService service;

    @PostMapping("/article/add")
    @CrossOrigin
    public Msg addArticle(Article article){
        return service.addArticle(article);
    }

    @PostMapping("/article/getArticleData")
    @CrossOrigin
    public Msg getArticleData(int page,int limit,String name){
        List<Article> articles=service.getArticleData(page,limit,name);
        int len=service.getArticleLen();
        return Msg.success().add("articles",articles).add("len",len);
    }

    @PostMapping("/article/getDetailsById")
    @CrossOrigin
    public Msg getDetailsById(int id){
        Article articles=service.getDetailsById(id);
        return Msg.success().add("article",articles);
    }

    @PostMapping("/getHotTags")
    @CrossOrigin
    public Msg getHotTags(){
        List<Map.Entry<String, Integer>> tags=service.getHotTags();
        List<String> tagname=new ArrayList<>();
        List<Integer> tagcount=new ArrayList<>();
        for (Map.Entry<String, Integer> tag : tags) {
            if(!tag.getKey().trim().equals("")){
                tagname.add(tag.getKey());
                tagcount.add(tag.getValue());
            }
        }
        return Msg.success().add("tagname",tagname).add("tagcount",tagcount);
    }

    @PostMapping("/markdown/uploadimg")
    @CrossOrigin
    public Msg uploadimg(@RequestParam("file") String file){
        return service.uploadimg(file);
    }

    @PostMapping("/markdown/delImg")
    @CrossOrigin
    public Msg delImg(@RequestParam("path") String path){
        return service.delImg(path);
    }

    @PostMapping("/markdown/image/getBase64ByUrl")
    @CrossOrigin
    public Msg getBase64ByUrl(@RequestParam("name") String[] name,@RequestParam("time") String time){
        return service.getBase64ByUrl(name,time);
    }

    @PostMapping("/getAllTitles")
    @CrossOrigin
    public Msg getAllTitles(){
        return service.getAllTitles();
    }

    @PostMapping("/article/getPublished")
    @CrossOrigin
    public Msg getPublished(String name){
        return service.getPublished(name);
    }

    @PostMapping("/article/deleteArticle")
    @CrossOrigin
    public Msg deleteArticle(int aid){
        return service.deleteArticle(aid);
    }

    @PostMapping("/article/getArticleInfo")
    @CrossOrigin
    public Msg getArticleInfo(int aid){
        return service.getArticleInfo(aid);
    }

    @PostMapping("/getEchartsData")
    @CrossOrigin
    public Msg getEchartsData(String name){
        return service.getEchartsData(name);
    }

    @PostMapping("/getAllTags")
    @CrossOrigin
    public Msg getAllTags(){
        return service.getAllTags();
    }

    @PostMapping("/getTagArticleData")
    @CrossOrigin
    public Msg getTagArticleData(String tagname,String name){
        return service.getTagArticleData(tagname, name);
    }

    @PostMapping("/article/addfavorites")
    @CrossOrigin
    public Msg addfavorites(int aid,String name){
        return service.addfavorites(aid, name);
    }

    @PostMapping("/article/hasfavorites")
    @CrossOrigin
    public Msg hasfavorites(int aid,String name){
        return service.hasfavorites(aid, name);
    }

    @PostMapping("/article/deletefavorites")
    @CrossOrigin
    public Msg deletefavorites(int aid,String name){
        return service.deletefavorites(aid, name);
    }

    @PostMapping("/article/getAllfavorites")
    @CrossOrigin
    public Msg getAllfavorites(String name){
        return service.getAllfavorites(name);
    }

    @PostMapping("/article/addDraft")
    @CrossOrigin
    public Msg addDraft(Article article){
        return service.addDraft(article);
    }

    @PostMapping("/article/getAllDraft")
    @CrossOrigin
    public Msg getAllDraft(String name){
        return service.getAllDraft(name);
    }

    @PostMapping("/article/deleteDraft")
    @CrossOrigin
    public Msg deleteDraft(int did){
        return service.deleteDraft(did);
    }

    @PostMapping("/article/getDraftInfo")
    @CrossOrigin
    public Msg getDraftInfo(int did){
        return service.getDraftInfo(did);
    }
}
