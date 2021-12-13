package com.zxqax.nblog.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ThreadUtils {
    public static ExecutorService getExecutorService(){
        return  Executors.newFixedThreadPool(2);
    }
}
