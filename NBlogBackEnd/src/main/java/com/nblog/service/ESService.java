package com.nblog.service;

import com.nblog.dto.Result;
import com.nblog.dto.ArticleDTO;

import java.util.List;
import java.util.Map;

/**
 * 搜索服务
 * @author liulin
 */

public interface ESService {

    /**
     * full text 搜索
     * @param content 检索内容
     * @param type 检索类型
     * @return 搜索结果
     */
    Result<List<Map<String,Object>>> search(String content, int type);

    /**
     * 增加文档
     * @param indexName 索引名
     * @param articleDTO Document 信息
     */
    void addDocument(String indexName, ArticleDTO articleDTO);

    /**
     * 删除文档
     * @param indexName 索引名
     * @param aid Document id
     */
    void deleteDocument(String indexName, String aid);
}
