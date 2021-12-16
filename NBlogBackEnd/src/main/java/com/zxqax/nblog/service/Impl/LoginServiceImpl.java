package com.zxqax.nblog.service.Impl;

import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.dto.UserDTO;
import com.zxqax.nblog.dao.LoginDao;
import com.zxqax.nblog.dao.UserDao;
import com.zxqax.nblog.entity.UserInfo;
import com.zxqax.nblog.service.LoginService;
import com.zxqax.nblog.service.RedisService;
import com.zxqax.nblog.service.UserService;
import com.zxqax.nblog.utils.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.zxqax.nblog.constant.MQPrefixConst.EMAIL_EXCHANGE;

@Service
public class LoginServiceImpl implements LoginService {

    UserDao userDao;

    LoginDao loginDao;

    RedisService redisService;

    UserService userService;

    RabbitTemplate rabbitTemplate;

    @Autowired
    public LoginServiceImpl(UserDao userDao,LoginDao loginDao,RedisService redisService,UserService userService,RabbitTemplate rabbitTemplate){
        this.userDao =userDao;
        this.loginDao =loginDao;
        this.redisService=redisService;
        this.userService=userService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 用户登录 登录方式 ：用户名 + 密码
     * @param name 用户名
     * @param pwd  用户密码 ，通过 sha 256 加密算法加密
     * @return 是否登录成功
     */
    @Override
    public Result<?> loginViaUserNameAndPassword(String name, String pwd) {

        if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(pwd)){

            // 走 邮箱 + 密码 登录
            if(EmailUtils.checkEmail(name)){
                // 检查邮箱是否存在
                int exist= userDao.checkIfTheEmailExists(name);

                // 不存在，直接返回
                if(exist==0){
                    return Result.fail("用户不存在，请先前往注册");
                }

                // 用户存在，获取用户 密码 + 用户名
                UserDTO user= loginDao.getPwdAndNameByEmail(name);

                // 返回结果
                if(user.getUPwd().equals(pwd)){
                    UserInfo current_user = userDao.getInfo(user.getUName());

                    String token = TokenUtils.token(name + pwd+ Math.random() * 100);
                    Map<String,Object> res = new HashMap<>();
                    res.put("user",current_user);
                    res.put("token",token);

                    userService.saveUser(token,current_user);

                    return Result.ok(res);
                }else {
                    return Result.fail("密码错误");
                }
            }else {
                // 走 用户名 + 密码 登录
                int exist= userDao.checkIfTheUserNameExists(name);

                if(exist==0){
                    return Result.fail("用户不存在，请先前往注册");
                }

                // 用户存在，获取用户信息
                UserInfo current_user = userDao.getInfo(name);

                if(current_user.getPwd().equals(pwd)){
                    String token = TokenUtils.token(name + pwd+ Math.random() * 120);
                    Map<String,Object> res = new HashMap<>();
                    res.put("user",current_user);
                    res.put("token",token);
                    userService.saveUser(token,current_user);

                    return Result.ok(res);
                }else {
                    return Result.fail("密码错误");
                }
            }
        }else {
            return Result.fail("用户名或密码为空");
        }
    }

    @Override
    public Result<?> loginViaEmail(String email) {

        if(EmailUtils.checkEmail(email)){

            // 检查邮箱是否存在
            int e= userDao.checkIfTheEmailExists(email);
            if(e==0){
                return Result.fail("邮箱不存在，请先前往注册");
            }

            String code= VerificationCodeUtils.getRandomVerificationCode(4);

            // 发送验证码,使用 rabbitmq 消息队列的方式
//            boolean has_send=EmailUtils.sendEmail(email,code);
            Map<String,String> message = new HashMap<>(2);
            message.put("email",email);
            message.put("code",code);
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE,"",message);

            String name = loginDao.getNameByEmail(email);
            // 用户存在，获取用户信息
            UserInfo current_user = userDao.getInfo(name);
            String token = TokenUtils.token(email + Math.random() * 120);

            Map<String,Object> res =new HashMap<>();
            res.put("code",code);
            res.put("user",current_user);
            res.put("token",token);
            userService.saveUser(token,current_user);

            return Result.ok(res);
        }else {
            return Result.fail("请输入正确的邮箱格式");
        }
    }

    /**
     * 退出登录
     * @return
     */
    @Override
    public Result<?> exit() {
        userService.exit();
        return Result.ok();
    }
}
