package com.nblog.service.Impl;

import com.nblog.constant.RedisPrefixConst;
import com.nblog.dao.UserDao;
import com.nblog.dao.UserRegisterDao;
import com.nblog.dto.Result;
import com.nblog.service.RedisService;
import com.nblog.service.UserRegisterService;
import com.nblog.utils.StringUtils;
import com.nblog.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {
    private final RedisService redisService;
    private final UserDao userDao;
    private final UserRegisterDao userRegisterDao;

    @Autowired
    public UserRegisterServiceImpl(RedisService redisService, UserDao userDao, UserRegisterDao userRegisterDao) {
        this.redisService = redisService;
        this.userDao = userDao;
        this.userRegisterDao = userRegisterDao;
    }

    @Override
    public Result<Integer> checkIfTheUserNameExists(String name) {
        // 从 Redis 种获取数据
        Object res = redisService.hGet(RedisPrefixConst.EXISTS_NAME,name);

        if(Objects.nonNull(res)){
            // 获取到，直接返回
            return Result.ok(Integer.parseInt(res.toString()));
        }else {
            // 从 MySQL 中获取
            int count= userDao.checkIfTheUserNameExists(name);
            // 保存到 Redis
            // Hash 方式保存用户存在信息
            redisService.hSet(RedisPrefixConst.EXISTS_NAME,name,count);

            return Result.ok(count);
        }
    }

    @Override
    public Result<Integer> checkIfTheEmailExists(String email) {
        // 从 Redis 种获取数据
        Object res = redisService.hGet(RedisPrefixConst.EXISTS_EMAIL,email);

        if(Objects.nonNull(res)){
            // 获取到，直接返回
            return Result.ok(Integer.parseInt(res.toString()));
        }else {
            // 从 MySQL 中获取
            int count= userDao.checkIfTheEmailExists(email);
            // 保存到 Redis
            // Hash 方式保存用户存在信息
            ThreadUtils.getExecutorService().execute(()->{
                redisService.hSet(RedisPrefixConst.EXISTS_EMAIL,email,count);
            });

            return Result.ok(count);
        }
    }

    @Override
    public Result<String> userRegistration(String name, String pwd, String email) {
        if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(pwd) && StringUtils.isNotBlank(email)){

            // 双重检查，再次检查是否重名或者重邮箱
            if(userDao.checkIfTheUserNameExists(name) + userDao.checkIfTheEmailExists(email)>0){
                return Result.fail("用户名或邮箱已存在");
            }

            // 插入数据进 Mysql
            userRegisterDao.userRegistration(name,pwd,email);

            ThreadUtils.getExecutorService().execute(()->{
                // 修改 Redis 中用户信息
                redisService.hSet(RedisPrefixConst.EXISTS_NAME,name,1);
                redisService.hSet(RedisPrefixConst.EXISTS_EMAIL,email,1);
            });

        }else {
            return Result.fail("用户名，密码，邮箱均不能为空");
        }

        return Result.ok();
    }
}
