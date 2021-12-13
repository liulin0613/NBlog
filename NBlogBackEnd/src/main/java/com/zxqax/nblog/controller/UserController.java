package com.zxqax.nblog.controller;

import com.zxqax.nblog.annotation.LogAnnotation;
import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.service.ImageService;
import com.zxqax.nblog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.zxqax.nblog.constant.OptTypeConst.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {

    UserService userService;
    ImageService imageService;

    @Autowired
    public UserController(UserService userService,ImageService imageService){
        this.userService=userService;
        this.imageService=imageService;
    }


    /**
     * 添加关注，用户 ${name} 关注用户 ${fname}
     * @param fid 被关注用户
     * @return addFollow true or false
     */
    @PostMapping("/addFollow")
    @CrossOrigin
    @ApiOperation(value = "添加关注")
    @LogAnnotation(optType = SAVE)
    public Result<?> addFollow(@RequestParam(value = "fid") int fid){
        return userService.addFollow(fid);
    }

    /**
     * 删除关注，用户 ${name} 删除关注用户 ${fname}
     * @param fid 被关注用户
     * @return addFollow true or false
     */
    @PostMapping("/deleteFollow")
    @CrossOrigin
    @ApiOperation(value = "删除关注")
    @LogAnnotation(optType = REMOVE)
    public Result<?> deleteFollow(@RequestParam(value = "fid") int fid){
        return userService.deleteFollow(fid);
    }

    /**
     * 用户 ${name} 添加 博文收藏 ${aid}
     * @param aid 博文 id
     * @return  Result addFavorites true or false
     */
    @PostMapping("/addFavorites")
    @CrossOrigin
    @ApiOperation(value = "添加 博文收藏 ")
    @LogAnnotation(optType = SAVE)
    public Result<?> addFavorites(@RequestParam(value = "aid") int aid){
        return userService.addFavorites(aid);
    }

    /**
     * 用户 ${name} 删除 博文收藏 ${aid}
     * @param aid 博文 id
     * @return  Result deleteFavorites true or false
     */
    @PostMapping("/deleteFavorites")
    @CrossOrigin
    @ApiOperation(value = "删除 博文收藏")
    @LogAnnotation(optType = REMOVE)
    public Result<?> deleteFavorites(@RequestParam(value = "aid") int aid){
        return userService.deleteFavorites(aid);
    }

    /**
     * 用户 ${name} 点赞 博文 ${aid}
     * @param aid 博文 id
     * @return  Result addLike true or false
     */
    @PostMapping("/addLike")
    @CrossOrigin
    public Result<?> addLike(@RequestParam(value = "aid") int aid){
        return userService.addLike(aid);
    }

    /**
     * 用户 ${name} 删除 博文 点赞 ${aid}
     * @param aid 博文 id
     * @return  Result deleteLike true or false
     */
    @PostMapping("/deleteLike")
    @CrossOrigin
    public Result<?> deleteLike(@RequestParam(value = "aid") int aid){
        return userService.deleteLike(aid);
    }

    /**
     * 检查用户的登录状态是否过期
     * @return  Result deleteFavorites true or false
     */
    @PostMapping("/checkExpiration")
    @CrossOrigin
    public Result<?> checkExpiration(){
        return userService.checkExpiration();
    }

    /**
     * 文章的信息统计
     * @return
     */
    @PostMapping("/getArticleInfo")
    @CrossOrigin
    public Result<?> getArticleInfo(){
        return userService.getArticleInfo();
    }

    /**
     * 获取社交信息
     * @return
     */
    @PostMapping("/getSocialInfo")
    @CrossOrigin
    public Result<?> getSocialInfo(){
        return userService.getSocialInfo();
    }

    /**
     * 修改头像
     * @return
     */
    @PostMapping("/alertAvatar")
    @CrossOrigin
    @ApiOperation(value = "修改头像")
    @LogAnnotation(optType = UPDATE)
    public Result<?> alertAvatar(String img){
        return imageService.alertAvatar(img);
    }

    /**
     * 修改用户名
     * @return
     */
    @PostMapping("/updateUserName")
    @CrossOrigin
    @ApiOperation(value = "修改用户名")
    @LogAnnotation(optType = UPDATE)
    public Result<?> updateUserName(String name){
        return userService.updateUserName(name);
    }

    /**
     * 修改用户简介
     * @return
     */
    @PostMapping("/updateUserIntro")
    @CrossOrigin
    @ApiOperation(value = "修改用户简介")
    @LogAnnotation(optType = UPDATE)
    public Result<?> updateUserIntro(String intro){
        return userService.updateUserIntro(intro);
    }

    /**
     * 修改用户密码
     * @return
     */
    @PostMapping("/updateUserPwd")
    @CrossOrigin
    @ApiOperation(value = "修改用户密码")
    @LogAnnotation(optType = UPDATE)
    public Result<?> updateUserPwd(String pwd){
        return userService.updateUserPwd(pwd);
    }

    /**
     *
     * @return
     */
    @PostMapping("/getAllFollow")
    @CrossOrigin
    public Result<?> getAllFollow(){
        return userService.getAllFollow();
    }

    @PostMapping("/getPublishedByID")
    @CrossOrigin
    public Result<?> getPublishedByID(int id){
        return userService.getPublishedByID(id);
    }

    @PostMapping("/addViews")
    @CrossOrigin
    public Result<?> addViews(){
        return userService.addViews();
    }

    @PostMapping("/getViews")
    @CrossOrigin
    public Result<?> getViews(){
        return userService.getViews();
    }
}