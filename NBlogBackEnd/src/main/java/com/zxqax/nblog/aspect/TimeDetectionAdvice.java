package com.zxqax.nblog.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 方法执行时间统计
 * @author liulin
 */

@Aspect
@Slf4j
@Component
public class TimeDetectionAdvice {

    String methodName;      // 方法名
    long startTime;         // 开始时间

    @Pointcut("@annotation(com.zxqax.nblog.annotation.TimeDetectionAnnotation)")
    private void timeDetection() {

    }

    @Before("timeDetection()")
    public void doBefore(JoinPoint joinPoint) {
        methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        startTime = System.currentTimeMillis();
    }

    @After("timeDetection()")
    public void doAfter() {
        long E_time = System.currentTimeMillis() - startTime;
        log.info("执行 " + methodName + " 耗时为：" + E_time + "ms");
    }
}
