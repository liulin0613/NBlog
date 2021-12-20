package com.nblog.controller;

import com.nblog.annotation.LogAnnotation;
import com.nblog.dto.ArticleDTO;
import com.nblog.dto.DirDTO;
import com.nblog.dto.Result;
import com.nblog.service.DirService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nblog.constant.OptTypeConst.REMOVE;
import static com.nblog.constant.OptTypeConst.SAVE;

/**
 * @author liulin
 * 与文件夹相关业务
 */
@RestController
@RequestMapping("/dir")
@Api(tags = "文件夹模块")
public class DirController {
    private final DirService dirService;

    @Autowired
    public DirController(DirService dirService) {
        this.dirService = dirService;
    }

    /**
     * 获取用户所有未归档文章
     * @return
     */
    @GetMapping("/getNotArchived")
    @ApiOperation(value = "未归档文章")
    public Result<List<ArticleDTO>> getNotArchived(){
        return dirService.getNotArchived();
    }

    /**
     * 获取用户所有文件夹
     * @return
     */
    @GetMapping("/getAllDirs")
    @ApiOperation(value = "所有文件夹")
    public Result<List<DirDTO>>  getDirs(){
        return dirService.getAllDirs();
    }

    /**
     * 获取用户文件夹内的文章
     * @return
     */
    @GetMapping("/getDirsDetails")
    @ApiOperation(value = "文件夹内的文章")
    public Result<List<ArticleDTO>>  getDirsDetails(@RequestParam(value = "aid") int aid){
        return dirService.getDirsDetails(aid);
    }

    /**
     * 根据标题和文件夹搜索文章
     * @return
     */
    @GetMapping("/searchByTitleAndDir")
    @ApiOperation(value = "根据标题和文件夹搜索")
    public Result<List<ArticleDTO>>  searchByTitleAndDir( @RequestParam(value = "title") String title,
                                           @RequestParam(value = "did") int did){
        return dirService.searchByTitleAndDir(title,did);
    }

    /**
     * 创建文件夹
     * @return
     */
    @PostMapping("/addDir")
    @ApiOperation(value = "创建文件夹")
    @LogAnnotation(optType = SAVE)
    public Result<Boolean>  addDir(@RequestParam(value = "dirName") String dirName){
        return dirService.addDir(dirName);
    }

    /**
     * 将 aid 移动到 did
     * @param aid
     * @param did
     * @return
     */
    @PutMapping("/removeArticle")
    @ApiOperation(value = "文件移动")
    public Result<Boolean>  removeArticle(@RequestParam(value = "aid") int aid,
                                    @RequestParam(value = "did") int did){
        return dirService.removeArticle(aid,did);
    }

    /**
     * 删除文件夹
     * @return
     */
    @DeleteMapping("/deleteDir")
    @ApiOperation(value = "删除文件夹")
    @LogAnnotation(optType = REMOVE)
    public Result<Boolean> deleteDir(@RequestParam(value = "did") int did){
        return dirService.deleteDir(did);
    }
}
