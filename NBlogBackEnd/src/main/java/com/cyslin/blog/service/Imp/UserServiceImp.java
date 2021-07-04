package com.cyslin.blog.service.Imp;

import com.cyslin.blog.bean.Article;
import com.cyslin.blog.bean.Msg;
import com.cyslin.blog.bean.User;
import com.cyslin.blog.mapper.UserMapper;
import com.cyslin.blog.service.UserService;
import com.cyslin.blog.utils.Email;
import com.cyslin.blog.utils.RandomYzm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserMapper mapper;

    @Override
    public Msg Checkname(String name) {
        int count=mapper.Checkname(name);
        return Msg.success().add("count",count);
    }

    @Override
    public Msg emailYzm(String email) {
        String code= RandomYzm.getStringRandom(4);
        Email.sendEmail(email,code);

        return Msg.success().add("code",code);
    }

    @Override
    public Msg addUser(String name, String pwd, String email) {
        Date date=new Date();
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time =df.format(date);
        mapper.addUser(name,pwd,email,time);
        return Msg.success();
    }

    @Override
    public Msg userLogin(String name, String pwd) {
        int hasUser=mapper.Checkname(name);
        if(hasUser==0){
            return Msg.fail().add("error","该用户不存在");
        }else {
            if(!pwd.equals(mapper.getPwdByName(name))){
                return Msg.fail().add("error","密码错误");
            }else {
                return Msg.success();
            }
        }
    }

    @Override
    public Msg emailLogin(String email) {
        String code= RandomYzm.getStringRandom(4);
        Email.sendEmail(email,code);
        String name=mapper.getNameByEmail(email);
        return Msg.success().add("code",code).add("name",name);

    }

    @Override
    public Msg emailExist(String email) {
        int hasEmail=mapper.Checkemail(email);
        if(hasEmail==0){
            return Msg.fail().add("error","该邮箱不存在");
        } else {
            return Msg.success();
        }
    }

    @Override
    public Msg addfollow(String name, String fname) {
        int uid=mapper.getuIdByName(name);
        int fid=mapper.getuIdByName(fname);
        mapper.addfollow(uid,fid);
        return Msg.success();
    }

    @Override
    public Msg hasfollow(String name, String fname) {
        int uid=mapper.getuIdByName(name);
        int fid=mapper.getuIdByName(fname);
        int count= mapper.hasfollow(uid,fid);
        return Msg.success().add("count",count);
    }

    @Override
    public Msg deletefollow(String name, String fname) {
        int uid=mapper.getuIdByName(name);
        int fid=mapper.getuIdByName(fname);
        mapper.deletefollow(uid,fid);
        return Msg.success();
    }

    @Override
    public Msg getAllfollow(String name) {
        int uid=mapper.getuIdByName(name);
        List<Integer> follows=mapper.getAllfollows(uid);
        List<User> users=new ArrayList<>();

        for (Integer follow : follows) {
            String uname=mapper.getNameByID(follow);
            String time=mapper.getTimeByID(follow);
            int followcount=mapper.getfollowcount(follow);
            int fanscount=mapper.getfanscount(follow);
            int articlecount=mapper.getarticlecount(uname);

            User user=new User();
            user.setArticlecount(articlecount);
            user.setFanscount(fanscount);
            user.setFollowcount(followcount);
            user.setName(uname);
            user.setTime(time);

            users.add(user);
        }
        return Msg.success().add("user",users);
    }

    @Override
    public Msg getPublishedByName(String name) {
        List<Article> articles=mapper.getPublishedByName(name);
        return Msg.success().add("article",articles);
    }

    @Override
    public Msg getinfo(String name) {
        String pwd=mapper.getPwdByName(name);
        String email=mapper.getEmailbyName(name);
        int card=1;
        int hasCard=mapper.hasCard(name);
        if(hasCard!=0){
            String time=mapper.getAlertTime(name);
            Date date=new Date();
            long currentTime=date.getTime();

            int days = (int) ((currentTime - Long.parseLong(time)) / (1000 * 60 * 60 * 24));
            if(days<=3){
                card=0;
            }

        }
        return Msg.success().add("pwd",pwd).add("email",email).add("card",card);
    }

    @Override
    public Msg alertName(String oldname, String name) {
        mapper.alertUserName(oldname,name);
        mapper.alertArticleName(oldname,name);
        mapper.alertDraftName(oldname,name);
        Date date=new Date();
        String time= date.getTime()+"";
        mapper.addAlertHistory(oldname,name,time);

        return Msg.success();
    }

    @Override
    public Msg alertPwd(String name, String pwd) {
        mapper.alertPwd(name,pwd);
        return Msg.success();
    }
}
