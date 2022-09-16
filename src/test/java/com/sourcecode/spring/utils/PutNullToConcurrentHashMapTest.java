package com.sourcecode.spring.utils;

import com.sourcecode.content.service.OrderService;
import com.sourcecode.content.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/16 14:29
 * @Version 1.0.0
 * @Description
 **/
public class PutNullToConcurrentHashMapTest {

    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>(256);


    @Test
    private void loadContext() {
        singletonObjects.put("userService", null);
        System.out.println(singletonObjects);

    }
}
