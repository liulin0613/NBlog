package com.nblog.dao;

import com.nblog.dto.TagArticleDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagDao {

    /**
     * 获取 标签
     * @return tags
     */
    List<String> getTags();

    /**
     * 获取 标签下的所有文章
     * @param tag 标签
     * @return ArticleDto
     */
    List<TagArticleDTO> getTagArticleData(String tag);
}
