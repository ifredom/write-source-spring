package com.sourcecode.spring.bean;

import com.sourcecode.content.service.UserService;
import com.sourcecode.content.service.UserServiceImpl;
import com.sourcecode.spring.aop.AopProxyHandle;
import com.sourcecode.spring.exception.BeansException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 初始化钩子
 * 作用：在Bean对象在实例化和依赖注入完毕后，实例初始化完成前/后 执行一些增强逻辑
 *
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/14 17:34
 * @Version 1.0.0
 * @Description
 **/
public interface BeanPostProcessor {

    /**
     * 初始化前
     *
     * @param bean     豆
     * @param beanName bean名字
     * @return {@link Object}
     * @throws BeansException 豆子异常
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 初始化后
     *
     * @param bean     豆
     * @param beanName bean名字
     * @return {@link Object}
     * @throws BeansException 豆子异常
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        // Aop切面实现。利用java-jdk动态代理.
        // 只有实现了 BeanPostProcessor ，并且的类

        if (bean instanceof BeanPostProcessor) {
            AopProxyHandle aopProxyHandle = new AopProxyHandle(bean);
            return aopProxyHandle.gerProxy();
        }


        return bean;
    }
}
