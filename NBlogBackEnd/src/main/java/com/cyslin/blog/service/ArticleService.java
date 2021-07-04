package com.cyslin.blog.service;

import com.cyslin.blog.bean.Article;
import com.cyslin.blog.bean.Msg;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    Msg addArticle(Article article);

    List<Article> getArticleData(int page, int limit,String name);

    Article getDetailsById(int id);

    List<Map.Entry<String, Integer>> getHotTags();

    Msg uploadimg(String file);

    Msg getBase64ByUrl(String[] name,String time);

    Msg delImg(String path);

    int getArticleLen();

    Msg getAllTitles();

    Msg getPublished(String name);

    Msg deleteArticle(int aid);

    Msg getArticleInfo(int aid);

    Msg getEchartsData(String name);

    Msg getAllTags();

    Msg getTagArticleData(String tagname,String name);

    Msg addfavorites(int aid, String name);

    Msg hasfavorites(int aid, String name);

    Msg deletefavorites(int aid, String name);

    Msg getAllfavorites(String name);

    Msg addDraft(Article article);

    Msg getAllDraft(String name);

    Msg deleteDraft(int did);

    Msg getDraftInfo(int did);
}
