package com.zxqax.nblog.annotation;

import java.lang.annotation.*;

/**
 * 敏感词检测
 * @author liulin
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveAnnotation {

}
