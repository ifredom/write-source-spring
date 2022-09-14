package com.sourcecode.spring.bean;

import com.sourcecode.spring.exception.BeansException;
import com.sun.istack.internal.Nullable;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/14 17:34
 * @Version 1.0.0
 * @Description
 **/
public interface BeanPostProcessor {

    /**
     * 发布过程初始化前
     * @param bean     豆
     * @param beanName bean名字
     * @return {@link Object}
     * @throws BeansException 豆子异常
     *
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 发布过程初始化后
     *
     * @param bean     豆
     * @param beanName bean名字
     * @return {@link Object}
     * @throws BeansException 豆子异常
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
