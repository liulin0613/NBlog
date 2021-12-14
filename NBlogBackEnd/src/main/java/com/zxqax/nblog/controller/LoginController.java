package com.zxqax.nblog.controller;

import com.zxqax.nblog.annotation.LogAnnotation;
import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.service.LoginService;
import com.zxqax.nblog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.zxqax.nblog.constant.OptTypeConst.LOGIN;
import static com.zxqax.nblog.constant.OptTypeConst.SEND;

@RestController
@RequestMapping("/login")
@Api(tags = "登录模块")
public class LoginController {
    /**
     * Spring IOC 自动注入 loginService,UserService 实例
     * 使用 Setter的方式注入，更加具有可变性
     */
    private LoginService loginService;
    private UserService userService;

    @Autowired
    public void setService(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService=userService;
    }

    /**
     * 检查用户名是否存在
     * @param name 传入用户名
     * @return 是否存在
     */
    @PostMapping("/checkIfTheUserNameExists")
    @ApiOperation(value = "检查用户名是否存在")
    public Result<?> checkIfTheUserNameExists(@RequestParam(value = "name") String name){
        return userService.checkIfTheUserNameExists(name);
    }

    /**
     * 检查邮箱是否存在
     * @param email 用户邮箱
     * @return 是否存在
     */
    @PostMapping("/checkIfTheEmailExists")
    @ApiOperation(value = "检查邮箱是否存在")
    public Result<?> checkIfTheEmailExists(@RequestParam(value = "email") String email){
        return userService.checkIfTheEmailExists(email);
    }

    /**
     * 发送邮箱验证码
     * @param email 传入用户邮箱
     * @return Result 信息 是否发送成功
     */
    @PostMapping("/sendEmailVerificationCode")
    @ApiOperation(value = "发送邮箱验证码")
    @LogAnnotation(optType = SEND)
    public Result<?> sendEmailVerificationCode(@RequestParam(value = "email") String email){
        return userService.sendEmailVerificationCode(email);
    }

    /**
     * 用户登录 登录方式 ：用户名 + 密码
     * @param name 用户名
     * @param pwd  用户密码 ，通过 sha 256 加密算法加密
     * @return 是否登录成功
     */
    @PostMapping("/loginViaUserNameAndPassword")
    @ApiOperation(value = "用户名 + 密码 登录")
    @LogAnnotation(optType = LOGIN)
    public Result<?> loginViaUserNameAndPassword(@RequestParam(value = "name") String name,
                                           @RequestParam(value = "pwd") String pwd){

        return loginService.loginViaUserNameAndPassword(name,pwd);
    }

    /**
     * loginViaEmail 用户登录 登录方式 ： 邮箱
     * @param email 用户邮箱
     * @return Msg 信息 其中 is_login 属性 表明是否登录成功
     */
    @PostMapping("/loginViaEmail")
    @ApiOperation(value = "邮箱登录")
    @LogAnnotation(optType = LOGIN)
    public Result<?> loginViaEmail(@RequestParam(value ="email") String email){
        return loginService.loginViaEmail(email);
    }

    @PostMapping("/exit")
    @ApiOperation(value = "退出登录")
    public Result<?> exit(){
        return loginService.exit();
    }
}
