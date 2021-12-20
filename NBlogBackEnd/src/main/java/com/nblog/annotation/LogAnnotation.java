package com.nblog.annotation;

import java.lang.annotation.*;

/**
 * 日志注解
 * @author liulin
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    /**
     * @return 操作类型
     */
    String optType() default "";
}
