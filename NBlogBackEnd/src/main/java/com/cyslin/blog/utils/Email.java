package com.cyslin.blog.utils;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Email{
    public static  void sendEmail(String toAdress, String yzm){
        Properties properties = new Properties();
        properties.setProperty("mail.host","smtp.qq.com");//发送邮箱服务器
        properties.setProperty("mail.smtp.port","465");//发送端口
        properties.setProperty("mail.smtp.auth","true");//是否开启权限控制
        properties.setProperty("mail.transport.protocol","smtp");//发送的协议是简单的邮件传输协议
        properties.setProperty("mail.smtp.ssl.enable","true");


        //建立两点之间的链接
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                //发件人邮件用户名、授权码
                return new PasswordAuthentication("liu.cysl@qq.com", "xkxjszndkhqxhffi");
            }
        });
        //创建邮件对象
        Message message = new MimeMessage(session);
        //设置发件人
        try {
            message.setFrom(new InternetAddress("liu.cysl@qq.com"));
            //设置收件人
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(toAdress));//收件人
            //设置主题
            message.setSubject("【NBlog】验证码");
            //设置邮件正文  第二个参数是邮件发送的类型
            StringBuilder sb=new StringBuilder();
            sb.append("<!DOCTYPE>\n" +
                    "<div bgcolor='#cd0000' style='border:1px solid #d9f4ee; font-size:25px; line-height:22px; padding-left:1px;padding-top:5px;   padding-bottom:5px;'>\n" +
                    "   <div style='width:950px;font-family:arial; '>欢迎使用nblog，您的验证码为：<br/><br/><br/><h2 style='color:green'>"+yzm+"</h2><br/><p style=\"font-size:15px;\">如不是您本人操作，请忽略。版权所有：nblog</p></div>\n" +
                    "       </div>");
            message.setContent(sb.toString(),"text/html;charset=UTF-8");
            //发送一封邮件
            Transport transport = session.getTransport();
            transport.connect("liu.cysl@qq.com","xkxjszndkhqxhffi");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }finally {

        }

    }
}