package com.nblog.service.Impl;

import com.nblog.constant.RedisPrefixConst;
import com.nblog.dao.UserDao;
import com.nblog.dao.UserLoginDao;
import com.nblog.dto.Audience;
import com.nblog.dto.Result;
import com.nblog.entity.UserInfo;
import com.nblog.service.RedisService;
import com.nblog.service.UserLoginService;
import com.nblog.service.UserService;
import com.nblog.utils.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.nblog.constant.MQPrefixConst.EMAIL_EXCHANGE;
import static com.nblog.constant.RedisPrefixConst.ONLINE;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    private final Audience audience;
    private final UserLoginDao userLoginDao;
    private final UserDao userDao;
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;
    private final RedisService redisService;

    @Autowired
    public UserLoginServiceImpl(Audience audience, UserLoginDao userLoginDao, UserDao userDao, UserService userService,
                                RabbitTemplate rabbitTemplate, RedisService redisService) {
        this.audience = audience;
        this.userLoginDao = userLoginDao;
        this.userDao = userDao;
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
        this.redisService = redisService;
    }

    @Override
    public Result<?> loginViaUserNameAndPassword(String name, String pwd) {

        UserInfo current_user; // 保存当前用户信息
        String token; // 保存 token 信息

        if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(pwd)){

            // 走 邮箱 + 密码 登录
            if(CommonUtils.checkEmail(name)){
                // 检查邮箱是否存在
                int exist= userDao.checkIfTheEmailExists(name);

                // 不存在，直接返回
                if(exist==0){
                    return Result.fail("用户不存在，请先前往注册");
                }

                // 用户存在，获取用户信息
                current_user = userLoginDao.getInfoByEmail(name);

                // 密码错误 返回结果
                if(!current_user.getPwd().equals(pwd)) {
                    return Result.fail("密码错误");
                }
            }else {
                // 走 用户名 + 密码 登录
                int exist= userDao.checkIfTheUserNameExists(name);

                if(exist==0){
                    return Result.fail("用户不存在，请先前往注册");
                }

                // 用户存在，获取用户信息
                current_user = userLoginDao.getInfoByName(name);

                // 密码错误 返回结果
                if(!current_user.getPwd().equals(pwd)){
                    return Result.fail("密码错误");
                }
            }

            String id = current_user.getId()+"";

            // 获取 token
            token = JwtTokenUtil.createJWT(id , current_user.getName(), audience);

            // 返回结果，附带 token 信息
            Map<String,Object> res = new HashMap<>();
            res.put("user",current_user);
            res.put("token",token);

            // redis 中保存在线用户信息
            ThreadUtils.getExecutorService().execute(()->{
                userService.saveUser(id,current_user);
            });

            return Result.ok(res);

        }else {
            return Result.fail("用户名或密码为空");
        }
    }

    @Override
    public Result<String> sendEmailCode(String email) {
        if(CommonUtils.checkEmail(email)){
            // 检查邮箱是否存在
            int e= userDao.checkIfTheEmailExists(email);
            if(e==0){
                return Result.fail("邮箱不存在，请先前往注册");
            }

            String code= CommonUtils.getRandomVerificationCode(4);

            // 发送验证码,使用 rabbitmq 消息队列的方式
            Map<String,String> message = new HashMap<>(2);
            message.put("email",email);
            message.put("code",code);
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE,"",message);

            // 生成 redis 保存验证码的 uuid
            String uuid = UUID.randomUUID().toString().substring(0,6);

            // 验证码保存到 redis
            ThreadUtils.getExecutorService().execute(()->{
                redisService.set(RedisPrefixConst.CODE_PREFIX+uuid,email+":"+code, RedisPrefixConst.CODE_EXPIRE_TIME);
            });

            return Result.ok(uuid);
        }else {
            return Result.fail("请输入正确的邮箱格式");
        }
    }

    @Override
    public Result<?> emailLoginVerification(String uuid,String code) {
        // 从 redis 中获取验证码信息
        Object obj = redisService.get(RedisPrefixConst.CODE_PREFIX+uuid);
        // 存在继续下一步
        if(Objects.nonNull(obj)){
            String emailAndCode = (String) obj;
            // 分离出验证码
            String real_code = emailAndCode.substring(emailAndCode.indexOf(":")+1);
            if(real_code.equalsIgnoreCase(code)){
                // 分离出邮箱
                String email = emailAndCode.substring(0,emailAndCode.indexOf(":"));

                // 获取用户信息
                UserInfo current_user = userLoginDao.getInfoByEmail(email);
                // 获取 token
                String token = JwtTokenUtil.createJWT(current_user.getId()+"" , current_user.getName(), audience);

                Map<String,Object> res =new HashMap<>();
                res.put("user",current_user);
                res.put("token",token);

                // redis 中保存在线用户信息
                ThreadUtils.getExecutorService().execute(()->{
                    userService.saveUser(current_user.getId()+"",current_user);
                });

                return Result.ok(res);
            }else {
                return Result.fail("验证码错误");
            }
        }else {
            return Result.fail("验证码已过期,请重新发送");
        }
    }

    @Override
    public Result<Boolean> loginOut() {
        // 获取当前登录用户信息
        UserInfo currentUser = userService.getCurrentUser();
        // 从 redis 中移除用户信息
        ThreadUtils.getExecutorService().execute(()->{
            redisService.hDel(ONLINE,currentUser.getId()+"");
        });

        return Result.ok(true);
    }
}
