package com.nblog.controller;

import com.nblog.annotation.LogAnnotation;
import com.nblog.annotation.PassToken;
import com.nblog.dto.AttDTO;
import com.nblog.dto.FavoriteDTO;
import com.nblog.dto.Result;
import com.nblog.service.UserLoginService;
import com.nblog.service.UserRegisterService;
import com.nblog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nblog.constant.OptTypeConst.*;

/**
 * @author liulin
 * 与用户操作相关
 *
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {
    private UserLoginService userLoginService;
    private UserRegisterService userRegisterService;
    private UserService userService;

    @Autowired
    public UserController(UserLoginService userLoginService, UserRegisterService userRegisterService, UserService userService) {
        this.userLoginService = userLoginService;
        this.userRegisterService = userRegisterService;
        this.userService = userService;
    }

    // ============= 与用户登录部分业务相关 ==================
    // ============= 由 UserLoginService 处理 ==============

    /**
     * 用户登录 登录方式 ：用户名 + 密码
     * @param name 用户名
     * @param pwd  用户密码 ，通过 sha 256 加密算法加密
     * @return 是否登录成功
     */
    @PostMapping("/loginViaUserNameAndPassword")
    @ApiOperation(value = "用户名 + 密码 登录")
    @LogAnnotation(optType = LOGIN)
    @PassToken
    public Result<?> loginViaUserNameAndPassword(@RequestParam(value = "name") String name,
                                                 @RequestParam(value = "pwd") String pwd){

        return userLoginService.loginViaUserNameAndPassword(name,pwd);
    }

    /**
     * sendEmailCode 发送邮箱验证码
     * @param email 用户邮箱
     * @return
     */
    @GetMapping("/sendEmailCode")
    @ApiOperation(value = "发送邮箱验证码")
    @LogAnnotation(optType = SEND)
    @PassToken
    public Result<String> sendEmailCode(@RequestParam(value ="email") String email){
        return userLoginService.sendEmailCode(email);
    }

    /**
     * 邮箱登录验证
     * @param uuid 绑定验证码的 uuid
     * @param code 用户输入的验证码
     * @return
     */
    @PostMapping("/emailLoginVerification")
    @ApiOperation(value = "邮箱登录验证")
    @PassToken
    public Result<?> emailLoginVerification(@RequestParam(value ="uuid") String uuid,
                                            @RequestParam(value ="code") String code){
        return userLoginService.emailLoginVerification(uuid,code);
    }

    /**
     * 用户退出登录
     * @return
     */
    @PutMapping("/loginOut")
    @ApiOperation(value = "退出登录")
    @LogAnnotation(optType = UPDATE)
    public Result<Boolean> loginOut(){
        return userLoginService.loginOut();
    }

    // ============= 与用户注册部分业务相关 ==================
    // ============= 由 UserRegisterService 处理 ==============

    /**
     * 检查用户名是否存在
     * @param name 传入用户名
     * @return 是否存在
     */
    @GetMapping("/checkIfTheUserNameExists")
    @ApiOperation(value = "检查用户名是否存在")
    @PassToken
    public Result<Integer> checkIfTheUserNameExists(@RequestParam(value = "name") String name){
        return userRegisterService.checkIfTheUserNameExists(name);
    }

    /**
     * 检查邮箱是否存在
     * @param email 用户邮箱
     * @return 是否存在
     */
    @GetMapping("/checkIfTheEmailExists")
    @ApiOperation(value = "检查邮箱是否存在")
    @PassToken
    public Result<Integer> checkIfTheEmailExists(@RequestParam(value = "email") String email){
        return userRegisterService.checkIfTheEmailExists(email);
    }

    /**
     * userRegistration 用户注册
     * @param name 用户名
     * @param pwd 用户密码，通过 sha 256 加密算法加密
     * @param email 用户注册邮箱
     * @return 是否注册成功
     */
    @PostMapping("/userRegistration")
    @ApiOperation(value = "用户注册")
    @LogAnnotation(optType = REGISTER)
    @PassToken
    public Result<String> userRegistration(@RequestParam(value = "name") String name,
                                      @RequestParam(value = "pwd") String pwd,
                                      @RequestParam(value = "email") String email){

        return userRegisterService.userRegistration(name,pwd,email);
    }


    // ============= 与用户信息部分业务相关 ==================
    // ============= 由 UserService 处理 ===================


    /**
     * 用户 添加 博文收藏 ${aid}
     * @param aid 博文 id
     * @return
     */
    @PostMapping("/addCollect")
    @ApiOperation(value = "添加 博文收藏 ")
    @LogAnnotation(optType = SAVE)
    public Result<Boolean> addCollect(@RequestParam(value = "aid") int aid){
        return userService.addCollect(aid);
    }

    /**
     * 用户删除博文收藏 ${aid}
     * @param aid 博文 id
     * @return
     */
    @DeleteMapping("/deleteCollect")
    @ApiOperation(value = "取消博文收藏")
    @LogAnnotation(optType = REMOVE)
    public Result<Boolean> deleteCollect(@RequestParam(value = "aid") int aid){
        return userService.deleteCollect(aid);
    }

    /**
     * 获取用户所有收藏的文章
     * @return
     */
    @GetMapping("/getAllCollects")
    @ApiOperation(value = "收藏的文章")
    public Result<List<FavoriteDTO>>  getAllCollects(){
        return userService.getAllCollects();
    }

    /**
     * 添加关注，用户 ${name} 关注用户 ${fname}
     * @param fid 被关注用户
     * @return
     */
    @PostMapping("/addAttention")
    @ApiOperation(value = "添加关注")
    @LogAnnotation(optType = SAVE)
    public Result<Boolean> addAttention(@RequestParam(value = "fid") int fid){
        return userService.addAttention(fid);
    }

    /**
     * 删除关注，用户 ${name} 删除关注用户 ${fname}
     * @param fid 被关注用户
     * @return
     */
    @DeleteMapping("/deleteAttention")
    @ApiOperation(value = "删除关注")
    @LogAnnotation(optType = REMOVE)
    public Result<Boolean> deleteAttention(@RequestParam(value = "fid") int fid){
        return userService.deleteAttention(fid);
    }

    /**
     * 获取所有关注的用户
     * @return
     */
    @GetMapping("/getAllAttention")
    @ApiOperation(value = "获取所有关注的用户")
    public Result<List<AttDTO>> getAllAttention(){
        return userService.getAllAttention();
    }

    /**
     * 用户 ${name} 点赞 博文 ${aid}
     * @param aid 博文 id
     * @return
     */
    @PostMapping("/addLike")
    @ApiOperation(value = "点赞")
    @LogAnnotation(optType = SAVE)
    public Result<Boolean> addLike(@RequestParam(value = "aid") int aid){
        return userService.addLike(aid);
    }

    /**
     * 用户 ${name}取消博文点赞 ${aid}
     * @param aid 博文 id
     * @return
     */
    @DeleteMapping("/deleteLike")
    @ApiOperation(value = "取消点赞")
    public Result<Boolean> deleteLike(@RequestParam(value = "aid") int aid){
        return userService.deleteLike(aid);
    }


    /**
     * 添加首页访问量
     * @return
     */
    @PostMapping("/addViews")
    @ApiOperation(value = "添加首页访问量")
    @PassToken
    public Result<Boolean> addViews(){
        return userService.addViews();
    }

    /**
     * 获取首页访问量
     * @return
     */
    @GetMapping("/getViews")
    @ApiOperation(value = "获取首页访问量")
    @PassToken
    public Result<Integer> getViews(){
        return userService.getViews();
    }

}
