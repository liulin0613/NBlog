package com.cyslin.blog.mapper;

import com.cyslin.blog.bean.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper {
    void addArticle(@Param("title") String aTitle,@Param("tags") String aTags, @Param("scope")String aScope,@Param("content") String aContent, @Param("time")String time, @Param("author")String aAuthor);

    List<Article> getArticleData(@Param("page") int page, @Param("limit")int limit,@Param("name")String name);

    Article getDetailsById(int id);

    List<String> getHotTags();

    int getArticleLen();

    List<Article> getAllTitles();

    int getIdByname(String aAuthor);

    List<Article> getPublished(String name);

    void deleteArticle(int aid);

    Article getArticleInfo(int aid);

    int getEchartsDataByMonth(@Param("name") String name, @Param("month") String month);

    int getEchartsDataByYear(@Param("name")String name,@Param("year")  String year);

    List<Article> getTagArticleData(@Param("tagname")String tagname, @Param("name")String name);

    void addfavorites(@Param("aid")int aid,@Param("uid") int uid,@Param("time") String time);

    int hasfavorites(@Param("aid")int aid,@Param("uid") int uid);

    void deletefavorites(@Param("aid")int aid,@Param("uid")int uid);

    List<Article> getAllfavorites(int uid);

    void addDraft(@Param("title")String aTitle,@Param("tags") String aTags,@Param("scope") String aScope, @Param("content")String aContent,@Param("time") String time, @Param("author") String aAuthor);

    List<Article> getAllDraft(String name);

    void deleteDraft(int did);

    Article getDraftInfo(int did);
}
