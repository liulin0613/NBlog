package com.nblog.annotation;

import java.lang.annotation.*;

/**
 * @author liulin
 * 被该注解修饰的方法不需要进行 token 验证
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PassToken {

}
