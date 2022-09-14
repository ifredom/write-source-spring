package com.sourcecode.spring.annotation;

import java.lang.annotation.*;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/14 11:29
 * @Version 1.0.0
 * @Description
 **/
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
}
