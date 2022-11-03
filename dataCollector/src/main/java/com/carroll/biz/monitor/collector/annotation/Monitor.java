package com.carroll.biz.monitor.collector.annotation;

import java.lang.annotation.*;

/**
 * Created on 2017/11/20 下午1:19 By hehongbo
 * <p>
 * 业务监控注解
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Monitor {

    String tag();
    String field() default "";
    String target() default "";
    long timeoutMs() default -1;
}
