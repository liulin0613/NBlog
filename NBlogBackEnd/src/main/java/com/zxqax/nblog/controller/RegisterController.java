package com.zxqax.nblog.controller;

import com.zxqax.nblog.annotation.LogAnnotation;
import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.zxqax.nblog.constant.OptTypeConst.LOGIN;
import static com.zxqax.nblog.constant.OptTypeConst.REGISTER;

@RestController
@RequestMapping("/register")
@Api(tags = "注册模块")
public class RegisterController {

    /**
     * Spring IOC 自动注入 registerService 实例
     * 使用 Setter的方式注入，更加具有可变性
     */
    RegisterService registerService;

    @Autowired
    public void setService(RegisterService registerService) {
        this.registerService = registerService;
    }


    /**
     * userRegistration 用户注册
     * @param name 用户名
     * @param pwd 用户密码，通过 sha 256 加密算法加密
     * @param email 用户注册邮箱
     * @return 是否注册成功
     */
    @PostMapping("/userRegistration")
    @CrossOrigin
    @ApiOperation(value = "用户注册")
    @LogAnnotation(optType = REGISTER)
    public Result<?> userRegistration(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "pwd") String pwd,
                                   @RequestParam(value = "email") String email){

        return registerService.userRegistration(name,pwd,email);
    }

}
