package com.sourcecode.spring.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/16 14:29
 * @Version 1.0.0
 * @Description
 **/
public class PutNullToConcurrentHashMapTest {

    private Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap(16));

    @Test
    @DisplayName("测试 ClassLoader 层级关系")
    void testPutNullToConcurrentHashMap() {

        String beanName = "userService";
        System.out.println("111111111111");
        if (!this.singletonsCurrentlyInCreation.add(beanName)) {
            System.out.println("x");
        } else {
            System.out.println("y");
        }
        System.out.println("success");

    }
}
