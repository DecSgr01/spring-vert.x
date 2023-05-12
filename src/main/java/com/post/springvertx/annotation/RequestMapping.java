package com.post.springvertx.annotation;


import java.lang.annotation.*;

/**
 * @author Administrator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequestMapping {
    String value() default "/";

    String method();
}