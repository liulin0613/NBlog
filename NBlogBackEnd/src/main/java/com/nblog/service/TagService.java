package com.nblog.service;

import com.nblog.dto.Result;
import com.nblog.dto.TagArticleDTO;
import com.nblog.dto.TagDTO;

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
     * @param tag 标签
     * @return Result
     */
    Result<List<TagArticleDTO>> getTagArticleData(String tag);
}
