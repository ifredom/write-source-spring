package com.sourcecode.spring.interfaces;

import com.sourcecode.spring.bean.BeanPostProcessor;

/**
 * 用于发生循环依赖时，提前对 bean 创建代理对象，这样注入的就是代理对象，而不是原始对象
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/20 9:01
 * @Version 1.0.0
 * @Description
 **/
public interface SmartInstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 如果 bean 需要被代理，返回代理对象；不需要被代理直接返回原始对象。
     *
     * @param bean     豆
     * @param beanName bean名字
     * @return {@link Object}
     * @throws RuntimeException 运行时异常
     */
    default Object getEarlyBeanReference(Object bean, String beanName) throws RuntimeException {
        return bean;
    }
}
