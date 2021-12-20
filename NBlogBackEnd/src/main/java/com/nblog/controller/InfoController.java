package com.nblog.controller;

import com.nblog.annotation.LogAnnotation;
import com.nblog.dto.Result;
import com.nblog.dto.SocialInfoDTO;
import com.nblog.service.ImageService;
import com.nblog.service.InfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.nblog.constant.OptTypeConst.UPDATE;

/**
 * @author liulin
 * 与用户的信息有关
 */

@RestController
@Api(tags = "信息模块")
@RequestMapping("/info")
public class InfoController {
    private final ImageService imageService;
    private final InfoService infoService;

    @Autowired
    public InfoController(ImageService imageService, InfoService infoService) {
        this.imageService = imageService;
        this.infoService = infoService;
    }

    /**
     * 修改头像
     * @return
     */
    @PutMapping("/updateAvatar")
    @ApiOperation(value = "修改头像")
    @LogAnnotation(optType = UPDATE)
    public Result<String> updateAvatar(@RequestParam("img") String img){
        return imageService.updateAvatar(img);
    }

    /**
     * 修改用户名
     * @return
     */
    @PutMapping("/updateUserName")
    @ApiOperation(value = "修改用户名")
    @LogAnnotation(optType = UPDATE)
    public Result<String> updateUserName(@RequestParam("name") String name){
        return infoService.updateUserName(name);
    }

    /**
     * 修改用户简介
     * @return
     */
    @PutMapping("/updateUserIntro")
    @ApiOperation(value = "修改用户简介")
    @LogAnnotation(optType = UPDATE)
    public Result<Boolean> updateUserIntro(@RequestParam("intro") String intro){
        return infoService.updateUserIntro(intro);
    }

    /**
     * 修改用户密码
     * @return
     */
    @PutMapping("/updateUserPwd")
    @ApiOperation(value = "修改用户密码")
    @LogAnnotation(optType = UPDATE)
    public Result<Boolean> updateUserPwd(@RequestParam("pwd") String pwd){
        return infoService.updateUserPwd(pwd);
    }

    /**
     * 检查用户的登录状态是否过期
     * @return
     */
    @GetMapping("/checkExpiration")
    @ApiOperation(value = "检查用户的登录状态是否过期")
    public Result<Boolean> checkExpiration(){
        return infoService.checkExpiration();
    }

    /**
     * 获取社交信息
     * @return
     */
    @GetMapping("/getSocialInfo")
    @ApiOperation(value = "获取社交信息")
    public Result<SocialInfoDTO> getSocialInfo(){
        return infoService.getSocialInfo();
    }
}
