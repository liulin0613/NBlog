package com.zxqax.nblog.aspect;

import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.utils.SensitiveUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 敏感词汇检测 Advice
 * @author liulin
 */

@Aspect
@Component
public class SensitiveAdvice {
    // 定义一个切面
    @Pointcut("@annotation(com.zxqax.nblog.annotation.SensitiveAnnotation)")
    private void SensitiveCheck() {

    }

    @Around("SensitiveCheck()")
    public Object permissionCheckFirst(ProceedingJoinPoint joinPoint) throws Throwable {

        //获取请求参数
        Object[] objects = joinPoint.getArgs();
        String s1 = (String) objects[0];

        // 检测是否有敏感词
        List<String> list = SensitiveUtils.filter(s1);
        if (list==null || list.isEmpty()) {
            return joinPoint.proceed();
        }else {
            return Result.fail(list,"含有敏感词汇");
        }
    }
}
