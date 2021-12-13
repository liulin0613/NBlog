package com.zxqax.nblog.utils;

import java.util.Random;

/**
 * 生成随机验证码，用于邮箱验证
 * 验证码由两种元素构成 ： 字母和数字
 */
public class VerificationCodeUtils {
    /**
     * @param length 验证码长度
     * @return 验证码字符串
     */
    public static String getRandomVerificationCode(int length) {

        StringBuilder val = new StringBuilder();

        // 从 ThreadLocalUtils 中获取线程安全的 Random 实例
        Random random= ThreadLocalUtils.localRandom.get();

        //length为几位密码
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
}
