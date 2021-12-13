package com.zxqax.nblog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.zxqax.nblog.constant.RedisPrefixConst;
import com.zxqax.nblog.dto.*;
import com.zxqax.nblog.dao.UserDao;
import com.zxqax.nblog.entity.UserInfo;
import com.zxqax.nblog.service.RedisService;
import com.zxqax.nblog.service.UserService;
import com.zxqax.nblog.utils.*;
import com.zxqax.nblog.vo.ArticleInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.zxqax.nblog.constant.RedisPrefixConst.ONLINE;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    RedisService redisService;

    @Autowired
    UserDao userDao;

    /**
     * 检查用户名是否存在
     * @param name 传入用户名
     * @return 是否存在 0 or 1
     */
    @Override
    public Result<?> checkIfTheUserNameExists(String name) {
        // 从 Redis 种获取数据
        Object res = redisService.hGet(RedisPrefixConst.EXISTS_NAME,name);

        if(Objects.nonNull(res)){
            // 获取到，直接返回
            return Result.ok(res);
        }else {
            // 从 MySQL 中获取
            int count= userDao.checkIfTheUserNameExists(name);
            // 保存到 Redis
            ThreadUtils.getExecutorService().execute(()->{
                // bgSave , Hash 方式保存用户存在信息
                redisService.hSet(RedisPrefixConst.EXISTS_NAME,name,count);
            });
            return Result.ok(count);
        }
    }

    /**
     * 检查邮箱是否存在
     * @param email 用户邮箱
     * @return 是否存在
     */
    @Override
    public Result<?> checkIfTheEmailExists(String email) {
        // 从 Redis 种获取数据
        Object res = redisService.hGet(RedisPrefixConst.EXISTS_EMAIL,email);

        if(Objects.nonNull(res)){
            // 获取到，直接返回
            return Result.ok(res);
        }else {
            // 从 MySQL 中获取
            int count= userDao.checkIfTheEmailExists(email);
            // 保存到 Redis
            ThreadUtils.getExecutorService().execute(()->{
                // bgSave , Hash 方式保存用户存在信息
                redisService.hSet(RedisPrefixConst.EXISTS_EMAIL,email,count);
            });
            return Result.ok(count);
        }
    }

    /**
     * 发送验证码
     * @param email 传入用户邮箱
     * @return code
     */
    @Override
    public Result<?> sendEmailVerificationCode(String email) {

        // 1. 获取验证码
        String code= VerificationCodeUtils.getRandomVerificationCode(4);
        // 2. 发送邮箱
        EmailUtils.sendEmail(email,code);

        return Result.ok(code);
    }

    /**
     * 判断用户 ${current_user_id} 是否收藏了博文 ${id}
     * @param current_user_id  name 用户
     * @param id 博文 id
     * @return isFavorites true or false
     */
    @Override
    public int getIs_Favorites(int current_user_id, int id) {
        return userDao.getIs_Favorites(current_user_id,id);
    }

    /**
     * 判断用户 ${current_user_id} 是否关注了用户 ${author_id}
     * @param current_user_id name id
     * @param author_id fname id
     * @return 是否关注  0 or 1
     */
    @Override
    public int getIs_Attention(int current_user_id, int author_id) {
        return userDao.getIs_Attention(current_user_id,author_id);
    }

    /**
     * 通过 token 获取当前线上用户信息
     * @return
     */
    @Override
    public UserInfo getCurrentUser() {

        // 从请求上下文里获取Request对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null){
            HttpServletRequest request = requestAttributes.getRequest();
            String hashKey = StringUtils.isNotBlank(request.getHeader("token"))?request.getHeader("token"):"-1";
            Object obj = redisService.hGet(ONLINE,hashKey);
            if(Objects.nonNull(obj)){
                return JSON.parseObject(obj.toString(),UserInfo.class);
            }
        }

        return new UserInfo(-1,"游客",null,null,null,null,null,null,null);
    }

    /**
     * 保存上线用户信息
     * @param token token
     * @param userInfo 用户信息
     */
    @Override
    public void saveUser(String token, UserInfo userInfo) {
        redisService.hSet(ONLINE,token,JSON.toJSONString(userInfo));
    }

    /**
     * 下线用户
     * @param token
     */
    @Override
    public void removeUser(String token) {
        redisService.hDel(ONLINE,token);
    }

    /**
     * 退出登录
     */
    @Override
    public void exit() {
        // 从请求上下文里获取Request对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null){
            HttpServletRequest request = requestAttributes.getRequest();
            String hashKey = StringUtils.isNotBlank(request.getHeader("token"))?request.getHeader("token"):"-1";
            redisService.hDel(ONLINE,hashKey);
        }
    }

    /**
     * 添加关注
     * @param fid 被关注用户
     * @return
     */
    @Override
    public Result<?> addFollow(int fid) {
        int current = getCurrentUser().getId();
        userDao.addFollow(current,fid);
        return Result.ok();
    }

    /**
     * 删除关注
     * @param fid 被关注用户
     * @return
     */
    @Override
    public Result<?> deleteFollow(int fid) {
        int current = getCurrentUser().getId();
        if(current != -1){
            userDao.deleteFollow(current,fid);
            return Result.ok();
        }else {
            return  Result.fail("登录信息错误");
        }
    }

    /**
     * 用户 ${name} 添加 博文收藏
     * @param aid 博文 id
     * @return
     */
    @Override
    public Result<?> addFavorites(int aid) {
        int current = getCurrentUser().getId();
        userDao.addFavorites(aid,current);
        return Result.ok();
    }

    /**
     * 户 ${name} 删除 博文收藏
     * @param aid 博文 id
     * @return
     */
    @Override
    public Result<?> deleteFavorites(int aid) {
        int current = getCurrentUser().getId();
        userDao.deleteFavorites(aid,current);
        return Result.ok();
    }

    @Override
    public Result<?> addLike(int aid) {
        int current = getCurrentUser().getId();
        userDao.addLike(aid,current);
        return Result.ok();
    }

    @Override
    public Result<?> deleteLike(int aid) {
        int current = getCurrentUser().getId();
        userDao.deleteLike(aid,current);
        return Result.ok();
    }

    /**
     * 检查用户的登录状态是否过期
     * @return
     */
    @Override
    public Result<?> checkExpiration() {
        UserInfo userInfo = getCurrentUser();
        boolean res = userInfo.getId() == -1;
        if(res){
            return Result.fail();
        }else {
            return Result.ok(userInfo);
        }
    }

    @Override
    public Result<?> getArticleInfo() {
        int user = getCurrentUser().getId();
        if(user != -1){
            ArticleInfoVO info = userDao.ArticleInfo(user);
            return Result.ok(info);
        }else {
            return Result.fail();
        }
    }

    @Override
    public Result<?> getSocialInfo() {
        int user = getCurrentUser().getId();
        if(user != -1){
            String location = getLocation();
            List<UserDTO> atts = userDao.getAtts(user);
            List<UserDTO> fans = userDao.getFans(user);

            SocialInfo info = new SocialInfo();
            info.setAtt(atts);
            info.setLocation(location);
            info.setAttCount(atts.size());
            info.setFans(fans);
            info.setFansCount(fans.size());

            return Result.ok(info);
        }else {
            return Result.fail();
        }
    }

    @Override
    public void updateImg(int id, String s) {
        userDao.updateImg(id,s);
    }

    @Override
    public Result<?> updateUserName(String name) {
        int user = getCurrentUser().getId();
        if(user != -1){
            int exists = userDao.checkIfTheUserNameExists(name);
            if(exists == 1){
                return Result.fail("用户名已存在");
            }else {
                userDao.updateUserName(user,name);
                return Result.ok();
            }
        }else {
            return Result.fail("状态错误");
        }
    }

    @Override
    public Result<?> updateUserIntro(String intro) {
        int user = getCurrentUser().getId();
        if(user != -1){
            userDao.updateUserIntro(user,intro);
            return Result.ok();
        }else {
            return Result.fail("状态错误");
        }
    }

    @Override
    public Result<?> updateUserPwd(String pwd) {
        int user = getCurrentUser().getId();
        if(user != -1){
            userDao.updateUserPwd(user,pwd);
            return Result.ok();
        }else {
            return Result.fail("状态错误");
        }
    }

    @Override
    public Result<?> getAllFollow() {
        int user = getCurrentUser().getId();
        if(user != -1){
            List<AttDTO> users=userDao.getAllFollow(user);
            return Result.ok(users);
        }else {
            return Result.fail("状态错误");
        }
    }

    @Override
    public Result<?> getPublishedByID(int id) {
        List<ArticleDTO> articles=userDao.getPublishedByID(id);
        return Result.ok(articles);
    }

    @Override
    public Result<?> addViews() {
        redisService.incr(RedisPrefixConst.VIEWS,1);
        return Result.ok();
    }

    @Override
    public Result<?> getViews() {
        return Result.ok(redisService.get(RedisPrefixConst.VIEWS));
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
        String ipAddress = IpUtils.getIpAddress(request);
        return IpUtils.getIpSource(ipAddress);
    }
}
