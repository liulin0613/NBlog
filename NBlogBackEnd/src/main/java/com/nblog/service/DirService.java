package com.nblog.service;

import com.nblog.dto.ArticleDTO;
import com.nblog.dto.DirDTO;
import com.nblog.dto.Result;

import java.util.List;

/**
 * 文件夹服务
 * @author liulin
 */

public interface DirService {

    /**
     * 获取未归档文章
     * @return
     */
    Result<List<ArticleDTO>> getNotArchived();

    /**
     * 获取文件夹
     * @return
     */
    Result<List<DirDTO>> getAllDirs();

    /**
     * 获取用户所有文件夹内的文章
     * @param aid
     * @return
     */
    Result<List<ArticleDTO>> getDirsDetails(int aid);

    /**
     *根据标题和文件夹搜索文章
     * @param title
     * @param did
     * @return
     */
    Result<List<ArticleDTO>> searchByTitleAndDir(String title, int did);

    /**
     * 创建文件夹
     * @param dirName
     * @return
     */
    Result<Boolean> addDir(String dirName);

    /**
     * 将 aid 移动到 did
     * @param aid
     * @param did
     * @return
     */
    Result<Boolean> removeArticle(int aid, int did);

    /**
     * 删除文件夹
     * @param did
     * @return
     */
    Result<Boolean> deleteDir(int did);
}
