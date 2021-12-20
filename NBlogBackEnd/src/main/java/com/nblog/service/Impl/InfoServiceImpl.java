package com.nblog.service.Impl;

import com.nblog.dao.InfoDao;
import com.nblog.dao.UserDao;
import com.nblog.dto.Result;
import com.nblog.dto.SocialInfoDTO;
import com.nblog.dto.UserDTO;
import com.nblog.entity.UserInfo;
import com.nblog.service.InfoService;
import com.nblog.service.RedisService;
import com.nblog.service.UserService;
import com.nblog.utils.IpUtils;
import com.nblog.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.nblog.constant.RedisPrefixConst.USER_ATTENTION;
import static com.nblog.constant.RedisPrefixConst.USER_FANS;

@Service
public class InfoServiceImpl implements InfoService {
    private UserService userService;
    private RedisService redisService;
    private UserDao userDao;
    private InfoDao infoDao;

    @Autowired
    public InfoServiceImpl(UserService userService, RedisService redisService, UserDao userDao, InfoDao infoDao) {
        this.userService = userService;
        this.redisService = redisService;
        this.userDao = userDao;
        this.infoDao = infoDao;
    }

    @Override
    public Result<String> updateUserName(String name) {
        // 获取当前用户
        UserInfo info = userService.getCurrentUser();
        int user = info.getId();

        // 判断用户名是否存在
        int exists = userDao.checkIfTheUserNameExists(name);
        if(exists == 1){
            return Result.fail("用户名已存在");
        }else {
            // 更新MySQL 中的用户名
            infoDao.updateUserName(user,name);

            // 异步更新 redis 中的用户信息
            info.setName(name);
            updateRedisInfo(info);

            //返回结果
            return Result.ok();
        }
    }

    @Override
    public Result<Boolean> updateUserIntro(String intro) {
        // 获取当前用户
        UserInfo info = userService.getCurrentUser();
        int user = info.getId();

        // MySQL 中更新用户信息
        infoDao.updateUserIntro(user,intro);

        // 异步更新 redis 中的用户信息
        info.setIntro(intro);
        updateRedisInfo(info);

        //返回结果
        return Result.ok(true);
    }

    @Override
    public Result<Boolean> updateUserPwd(String pwd) {
        // 获取当前用户
        UserInfo info = userService.getCurrentUser();
        int user = info.getId();

        // MySQL 中更新用户信息
        infoDao.updateUserPwd(user,pwd);

        // 异步更新 redis 中的用户信息
        info.setPwd(pwd);
        updateRedisInfo(info);

        //返回结果
        return Result.ok(true);
    }

    @Override
    public Result<Boolean> checkExpiration() {
        // 从 redis 中获取用户信息
        UserInfo userInfo = userService.getCurrentUser();
        // 判断是否在线 id != -1 ?
        boolean res = userInfo.getId() == -1;
        // 返回结果
        return Result.ok(res);
    }

    @Override
    public Result<SocialInfoDTO> getSocialInfo() {
        // 关注列表信息
        List<UserDTO> atts = new ArrayList<>();
        // 粉丝列表信息
        List<UserDTO> fans = new ArrayList<>();

        // 获取当前用户
        int user = userService.getCurrentUser().getId();
        // 获取地理位置
        String location = getLocation();

        // 从 redis 中获取社交关系
        // 关注信息
        Set<Object> att = redisService.sMembers(USER_ATTENTION + user);
        // 粉丝信息
        Set<Object> fan = redisService.sMembers(USER_FANS + user);

        // 用户 id
        int[] att_ids = new int[att.size()];
        int[] fan_ids = new int[fan.size()];

        // 从 MySQL 中获取关注信息
        int index = 0;
        if(att.size()!=0){
            for (Object object : att) {
                // 排除自己
                if(Integer.parseInt(object.toString()) != user){
                    att_ids[index++]=Integer.parseInt(object.toString());
                }
            }

            atts = infoDao.getSocialInfo(att_ids);
        }

        // 从 MySQL 中获取粉丝信息
        index = 0;
        if(fan.size()!=0){
            for (Object object : fan) {
                // 排除自己
                if(Integer.parseInt(object.toString()) != user){
                    fan_ids[index++]=Integer.parseInt(object.toString());
                }
            }

            fans = infoDao.getSocialInfo(fan_ids);
        }

        // 封装结果
        SocialInfoDTO info = new SocialInfoDTO();
        info.setAtt(atts);
        info.setLocation(location);
        info.setAttCount(atts.size());
        info.setFans(fans);
        info.setFansCount(fans.size());

        // 返回结果
        return Result.ok(info);
    }

    /**
     * 更新 redis 中的用户信息
     * @param info
     */
    public void updateRedisInfo(UserInfo info) {
        ThreadUtils.getExecutorService().execute(
                ()-> userService.saveUser(info.getId()+"",info));
    }

    /**
     * 获取地址信息
     * @return
     */
    private String getLocation(){
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        // 获取 IP 地址
        String ipAddress = IpUtils.getIpAddress(request);
        // 返回 IP 所在的位置
        return IpUtils.getIpSource(ipAddress);
    }
}
