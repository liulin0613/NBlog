package com.nblog.service.Impl;

import com.nblog.dao.DirDao;
import com.nblog.dto.ArticleDTO;
import com.nblog.dto.DirDTO;
import com.nblog.dto.Result;
import com.nblog.service.DirService;
import com.nblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirServiceImpl implements DirService {
    private UserService userService;
    private DirDao dirDao;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setDirDao(DirDao dirDao) {
        this.dirDao = dirDao;
    }

    @Override
    public Result<List<ArticleDTO>> getNotArchived() {
        // 获取当前用户
        int user = userService.getCurrentUser().getId();

        // 从 MySQL 中获取未归档文章
        List<ArticleDTO> archived = dirDao.getNotArchived(user);

        // 返回结果
        return Result.ok(archived);
    }

    @Override
    public Result<List<DirDTO>> getAllDirs() {
        // 获取当前用户
        int user = userService.getCurrentUser().getId();

        // 从 MySQL 中获取文件夹信息
        List<DirDTO> archived = dirDao.getDirs(user);

        // 返回结果
        return Result.ok(archived);
    }

    @Override
    public Result<List<ArticleDTO>> getDirsDetails(int aid) {
        // 获取当前用户
        int user = userService.getCurrentUser().getId();

        // 从 MySQL 中获取文章信息
        List<ArticleDTO> articleDTOList = dirDao.getDirsDetails(user,aid);

        // 返回结果
        return Result.ok(articleDTOList);
    }

    @Override
    public Result<List<ArticleDTO>> searchByTitleAndDir(String title, int did) {
        // 获取当前用户
        int user = userService.getCurrentUser().getId();

        // 结果的存储集合
        List<ArticleDTO> articleDTOList;

        // 搜索时如果 did == -1 ,则不指定文件夹搜索
        if(did == -1){
            // 任意文件夹可搜索
            articleDTOList = dirDao.searchByTitle(user,title);
        }else{
            // 根据指定文件夹和标题搜索
            articleDTOList = dirDao.searchByTitleAndDir(user,title,did);
        }

        // 返回结果
        return Result.ok(articleDTOList);
    }

    @Override
    public Result<Boolean> addDir(String dirName) {
        // 获取当前用户
        int user = userService.getCurrentUser().getId();

        // mysql 中新建文件夹数据
        dirDao.addDir(user,dirName);

        // 返回结果
        return Result.ok(true);
    }

    @Override
    public Result<Boolean> removeArticle(int aid, int did) {
        // mysql 中修改文件夹字段
        dirDao.removeArticle(aid,did);

        // 返回结果
        return Result.ok(true);
    }

    @Override
    public Result<Boolean> deleteDir(int did) {
        // 将当前文件夹下的文章移动到 未归档
        dirDao.updateDir(did);

        // 删除当前文件夹
        dirDao.deleteDir(did);

        // 返回结果
        return Result.ok(true);
    }
}
