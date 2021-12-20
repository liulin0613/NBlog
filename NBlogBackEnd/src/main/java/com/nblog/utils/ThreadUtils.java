package com.nblog.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liulin
 * 固定线程数量的线程池
 * 用来做  bg-save redis 数据
 */
@Component
public class ThreadUtils {
    public static ExecutorService getExecutorService(){
        return  Executors.newFixedThreadPool(4);
    }
}
