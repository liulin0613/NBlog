package com.zxqax.nblog.service;

import com.zxqax.nblog.dto.ArticleDTO;
import com.zxqax.nblog.dto.Result;

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
    Result<?> search(String content, int type);

    /**
     * 增加文档
     * @param indexName 索引名
     * @param articleDto Document 信息
     */
    void addDocument(String indexName, ArticleDTO articleDto);

    /**
     * 删除文档
     * @param indexName 索引名
     * @param aid Document id
     */
    void deleteDocument(String indexName,String aid);
}
