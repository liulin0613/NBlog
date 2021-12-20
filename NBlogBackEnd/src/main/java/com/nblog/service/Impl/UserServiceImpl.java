package com.nblog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.nblog.constant.RedisPrefixConst;
import com.nblog.dao.UserDao;
import com.nblog.dto.AttDTO;
import com.nblog.dto.Audience;
import com.nblog.dto.FavoriteDTO;
import com.nblog.dto.Result;
import com.nblog.entity.UserInfo;
import com.nblog.service.RedisService;
import com.nblog.service.UserService;
import com.nblog.utils.JwtTokenUtil;
import com.nblog.utils.StringUtils;
import com.nblog.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.nblog.constant.CommonConst.AUTH_HEADER_KEY;
import static com.nblog.constant.CommonConst.TOKEN_PREFIX;
import static com.nblog.constant.RedisPrefixConst.*;

@Service
public class UserServiceImpl implements UserService {
    private final Audience audience;
    private final RedisService redisService;
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(Audience audience, RedisService redisService, UserDao userDao) {
        this.audience = audience;
        this.redisService = redisService;
        this.userDao = userDao;
    }

    @Override
    public void saveUser(String id, UserInfo info) {
        // redis 中使用 hash 保存在线用户信息
        redisService.hSet(ONLINE,id, JSON.toJSONString(info));
    }

    @Override
    public UserInfo getCurrentUser() {
        // 从请求上下文里获取Request对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null){

            HttpServletRequest request = requestAttributes.getRequest();
            // 获取请求头中的验证对象
            String authHeader = request.getHeader(AUTH_HEADER_KEY);
            if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(TOKEN_PREFIX)) {
                return new UserInfo(-1,"游客",null,null,null,null,null,null,null);
            }

            // 获取token
            final String token = authHeader.substring(7);

            String userId = JwtTokenUtil.getUserId(token, audience.getBase64Secret());

            // 从 redis 中获取用户信息
            Object obj = redisService.hGet(ONLINE,userId);
            if(Objects.nonNull(obj)){
                return JSON.parseObject(obj.toString(),UserInfo.class);
            }
        }

        return new UserInfo(-1,"游客",null,null,null,null,null,null,null);
    }

    @Override
    public Result<List<FavoriteDTO>> getAllCollects() {
        // 保存结果集
        List<FavoriteDTO> favorites = new ArrayList<>();

        // 获取用户id
        int user = getCurrentUser().getId();

        // 从 redis 中获取收藏的文章 ID
        Set<Object> objects = redisService.sMembers(RedisPrefixConst.USER_COLLECT + user);
        if(objects.size()!=0){
            int[] ids = new int[objects.size()];
            int index = 0;
            for (Object object : objects) {
                ids[index++]=Integer.parseInt(object.toString());
            }

            // 从 mysql 中获取文章信息
            favorites = userDao.getAllCollects(ids);
        }

        return Result.ok(favorites);
    }

    @Override
    public Result<Boolean> addCollect(int aid) {
        // 获取当前用户
        UserInfo info = getCurrentUser();
        int id = info.getId();

        // 将收藏信息存入 redis
        if(!isSetContains(USER_COLLECT+id,aid)){
            ThreadUtils.getExecutorService().execute (()-> {
                // add 用户的收藏集合 add
                redisService.sAdd(USER_COLLECT+id,aid);
                // 文章的总收藏数 +1
                redisService.hIncr(Article_COLLECT,aid+"",1L);
            });
        }

        return Result.ok(true);
    }

    @Override
    public Result<Boolean> deleteCollect(int aid) {
        // 获取当前用户
        UserInfo info = getCurrentUser();
        int id = info.getId();

        // redis 中删除收藏信息
        if(isSetContains(USER_COLLECT+id,aid)){
            ThreadUtils.getExecutorService().execute (()-> {
                // 用户删除收藏
                redisService.sRemove(USER_COLLECT+id,aid);
                // 文章的总收藏数 -1
                redisService.hDecr(Article_COLLECT,aid+"",1L);
            });
        }

        return Result.ok(true);
    }

    @Override
    public void updateImg(int id, String s) {
        // 修改头像
        userDao.updateImg(id,s);
    }

    @Override
    public Result<Boolean> addAttention(int fid) {
        // 获取当前用户
        int current = getCurrentUser().getId();

        // 关注信息保存到 redis
        if(!isSetContains(USER_ATTENTION+current,fid)){
            ThreadUtils.getExecutorService().execute(()->{
                // 当前用户添加关注
                redisService.sAdd(USER_ATTENTION+current,fid);
                // fid 添加粉丝
                redisService.sAdd(USER_FANS+fid,current);
            });
        }

        return Result.ok(true);
    }

    @Override
    public Result<Boolean> deleteAttention(int fid) {
        // 获取当前用户
        int current = getCurrentUser().getId();

        // redis 中删除关注信息
        if(isSetContains(USER_ATTENTION+current,fid)){
            ThreadUtils.getExecutorService().execute(()->{
                // 当前用户删除关注
                redisService.sRemove(USER_ATTENTION+current,fid);
                // fid 删除粉丝
                redisService.sRemove(USER_FANS+fid,current);
            });
        }

        return Result.ok(true);
    }

    @Override
    public Result<List<AttDTO>> getAllAttention() {
        // 保存结果集
        List<AttDTO> users = new ArrayList<>();

        // 获取当前用户
        int current = getCurrentUser().getId();
        // 从 redis 中获取关注列表
        Set<Object> members = redisService.sMembers(USER_ATTENTION + current);
        // 如果关注列表不为空
        if(members.size()!=0){
            // 获取关注用户的 id
            int[] ids = new int[members.size()];
            int index = 0;
            for (Object object : members) {
                ids[index++]=Integer.parseInt(object.toString());
            }

            // 从 MySQL 中获取关注用户的基本信息
            users = userDao.getAllAttention(ids);

            // 从 redis 补充用户的社交信息
            for (AttDTO user : users) {
                // intro 填充 "" 代替 null
                if(StringUtils.isBlank(user.getIntro())){
                    user.setIntro("");
                }

                // 用户的关注数量
                Set<Object> att = redisService.sMembers(USER_ATTENTION + user.getId());
                user.setAttCount(att.size());

                // 用户的粉丝数量
                Set<Object> fans = redisService.sMembers(USER_FANS + user.getId());
                user.setFansCount(fans.size());

            }
        }

        return Result.ok(users);
    }

    @Override
    public Result<Boolean> addLike(int aid) {
        // 获取当前用户
        int current = getCurrentUser().getId();

        // 将点赞信息存入 redis
        if(!isSetContains(USER_LIKE+current,aid)){
            ThreadUtils.getExecutorService().execute (()-> {
                // add 用户的点赞集合 add
                redisService.sAdd(USER_LIKE+current,aid);
                // 文章的总点赞数 +1
                redisService.hIncr(Article_LIKE,aid+"",1L);
            });
        }

        return Result.ok(true);
    }

    @Override
    public Result<Boolean> deleteLike(int aid) {
        // 获取当前用户
        int current = getCurrentUser().getId();

        // redis 中异步删除点赞信息
        if(isSetContains(USER_LIKE+current,aid)){
            ThreadUtils.getExecutorService().execute (()-> {
                // remove 用户的点赞集合
                redisService.sRemove(USER_LIKE+current,aid);
                // 文章的总点赞数 -1
                redisService.hDecr(Article_LIKE,aid+"",1L);
            });
        }

        return Result.ok(true);
    }

    @Override
    public Result<Boolean> addViews() {
        // redis 中访问量 +1
        redisService.incr(VIEWS,1);
        // 返回结果
        return Result.ok(true);
    }

    @Override
    public Result<Integer> getViews() {
        // 从 redis 中获取访问量并返回
        Object o = redisService.get(VIEWS);
        int res = o == null?0:Integer.parseInt(o.toString());

        return Result.ok(res);
    }

    /**
     * 判断集合中是否包含某个元素
     * @param key
     * @param id
     * @return
     */
    private Boolean isSetContains(String key,int id){
        Set<Object> members = redisService.sMembers(key);
        return members.contains(id);
    }
}
