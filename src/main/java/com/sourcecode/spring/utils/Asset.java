package com.sourcecode.spring.utils;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/14 11:10
 * @Version 1.0.0
 * @Description
 **/
public class Asset {
    public static void notNull( Object object) {
        if (object == null) {
            throw new IllegalArgumentException("对象不能为空");
        }
    }
}
