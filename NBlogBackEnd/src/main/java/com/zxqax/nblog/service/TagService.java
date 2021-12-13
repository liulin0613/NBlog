package com.zxqax.nblog.service;

import com.zxqax.nblog.dto.ArticleDTO;
import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.dto.TagArticleDTO;
import com.zxqax.nblog.dto.TagDTO;

import java.util.List;

public interface TagService {
    /**
     * 获取热点标签
     * @return tags
     */
    Result<List<TagDTO>> getHotTags();

    /**
     * 获取所有标签
     * @return tags
     */
    Result<List<TagDTO>> getAllTags();

    /**
     * 获取 标签下的所有文章
     * @param tagname 标签
     * @return Result
     */
    Result<List<TagArticleDTO>> getTagArticleData(String tagname);
}
