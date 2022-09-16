package com.sourcecode.spring.bean;

import javax.naming.spi.ObjectFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/16 10:40
 * @Version 1.0.0
 * @Description
 **/
public class DefaultSingletonBeanRegistry {


    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    private Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);

    private Map<String, ObjectFactory> singletonFactories = new HashMap<>(16);
}
