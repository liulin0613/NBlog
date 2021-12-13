package com.zxqax.nblog.annotation;

import java.lang.annotation.*;

/**
 * 方法运行时间检测
 * @author liulin
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimeDetectionAnnotation {
}
