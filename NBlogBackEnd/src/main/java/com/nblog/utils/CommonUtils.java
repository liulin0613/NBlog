package com.nblog.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonUtils {

    // 用 ThreadLocal 保证 Random 线程安全
    public static ThreadLocal<Random> localRandom=ThreadLocal.withInitial(Random::new);

    /**
     * @param length 验证码长度
     * @return 验证码字符串
     */
    public static String getRandomVerificationCode(int length) {

        StringBuilder val = new StringBuilder();

        // 从 ThreadLocalUtils 中获取线程安全的 Random 实例
        Random random= localRandom.get();

        // length为几位密码
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }


    /**
     * 检测邮箱是否合法
     *
     * @param email 邮箱
     * @return 合法状态
     */
    public static boolean checkEmail(String email) {
        String rule = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(rule);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }

    /**
     * 合法化文件名
     * @param fileName
     * @return
     */
    public static String regFileName(String fileName){
        fileName=fileName.trim();
        //  不支持的文件|文件夹命名      / \ : * " < > | ？
        fileName=fileName.replaceAll(" ","");
        fileName=fileName.replaceAll("/","_");
        fileName=fileName.replaceAll("\\\\","_");
        fileName=fileName.replaceAll(":","_");
        fileName=fileName.replaceAll("\\*","_");
        fileName=fileName.replaceAll("\"","_");
        fileName=fileName.replaceAll("：","_");
        fileName=fileName.replaceAll("<","_");
        fileName=fileName.replaceAll(">","_");
        fileName=fileName.replaceAll("\\|","_");
        fileName=fileName.replaceAll("\\?","_");
        fileName=fileName.replaceAll("[.]","_");
        return fileName;
    }
}
