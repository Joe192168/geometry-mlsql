package com.geominfo.mlsql.aop;

import com.geominfo.authing.common.enums.EnumOperateLogType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


@Target(ElementType.METHOD)//该注解表示自定义注解可以在那些地方使用
@Retention(RetentionPolicy.RUNTIME)//自定义注解何时有效
@Inherited//表示一个类如果使用@Inherited注解标志的注解@GeometryLogAnno，该类的子类也会继承到这个注解
@Documented//可以生成文档
public @interface GeometryLogAnno {

    /**
     * 日志内容
     * @return
     */
    @AliasFor("desc")
    String value() default "";

    /**
     * 日志描述
     * @return
     */
    @AliasFor("value")
    String desc() default "";

    /**
     * 操作类型
     * @return
     */
    EnumOperateLogType operateType();

    /**
     * 操作用户id
     * @return
     */
    String operateUserId() default "";

    /**
     * 是否不记录日志
     * @return
     */
    boolean ignore() default false;
}
