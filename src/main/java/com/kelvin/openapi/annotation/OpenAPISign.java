/*
 * Copyright  2017. .com All Rights Reserved. 
 * Application : API 
 * Class Name  : Sign.java
 * Date Created: 2017年1月20日
 * Author      : Kelvin范显
 * 
 */
package com.kelvin.openapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加该注解，表示符合切面校验MD5码的方法
 * 
 * @author Kelvin范显
 * @createDate 2017年1月20日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenAPISign {


    /**
     * 默认每个时间窗口允许30次请求
     * @return
     */
    long rateLimitCount() default 30;

    /**
     * 默认时间窗口为1秒
     * @return
     */
    long rateLimitTimeWindow() default 1000;
}
