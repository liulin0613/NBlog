package com.zxqax.nblog.utils;

import com.zxqax.nblog.entity.UserInfo;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Random;

import static com.zxqax.nblog.constant.CommonConst.DEFAULT_AVATAR;

/**
 * ThreadLocal 提供了线程内存储变量
 * ThreadLocal 用途之一  每个线程需要一个独享的对象
 * ThreadLocal 用途之二 避免参数传递的麻烦
 */

@Component
public class ThreadLocalUtils {

    // 所有的线程都共用同一个 simpleDateFormat 对象（这是线程不安全的，出现了并发安全问题）
    // 用 ThreadLocal 保证 SimpleDateFormat 线程安全
    public static ThreadLocal<SimpleDateFormat> localSimpleDataFormat=
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static ThreadLocal<SimpleDateFormat> localSimpleDataFormatOnlyYMD=
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

    // 用 ThreadLocal 保证 Random 线程安全
    public static ThreadLocal<Random> localRandom=ThreadLocal.withInitial(Random::new);
}
