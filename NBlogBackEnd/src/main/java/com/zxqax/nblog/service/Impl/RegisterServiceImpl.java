package com.zxqax.nblog.service.Impl;

import com.zxqax.nblog.annotation.LogAnnotation;
import com.zxqax.nblog.annotation.TimeDetectionAnnotation;
import com.zxqax.nblog.constant.RedisPrefixConst;
import com.zxqax.nblog.dao.RegisterDao;
import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.dao.UserDao;
import com.zxqax.nblog.service.RedisService;
import com.zxqax.nblog.service.RegisterService;
import com.zxqax.nblog.utils.StringUtils;
import com.zxqax.nblog.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    UserDao userDao;

    @Autowired
    RegisterDao registerDao;

    @Autowired
    RedisService redisService;

    /**
     * 用户注册
     * @param name 用户名
     * @param pwd 用户密码，通过 sha 256 加密算法加密
     * @param email 用户注册邮箱
     * @return 注册
     */
    @Override
    public Result<?> userRegistration(String name, String pwd, String email) {

        if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(pwd) && StringUtils.isNotBlank(email)){

            // 双重检查，再次检查是否重名或者重邮箱
            if(userDao.checkIfTheUserNameExists(name) + userDao.checkIfTheEmailExists(email)>0){
                return Result.fail("用户名或邮箱已存在");
            }

            // 插入数据进 Mysql
            registerDao.userRegistration(name,pwd,email);

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
