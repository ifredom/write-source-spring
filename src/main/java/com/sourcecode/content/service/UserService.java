package com.sourcecode.content.service;

import com.sourcecode.spring.annotation.Autowired;
import com.sourcecode.spring.annotation.Component;
import com.sourcecode.spring.annotation.Scope;
import com.sourcecode.spring.bean.BeanNameAware;
import com.sourcecode.spring.bean.BeanPostProcessor;
import com.sourcecode.spring.exception.BeansException;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/12 17:31
 * @Version 1.0.0
 * @Description
 **/
@Component("userService")
@Scope("prototype")
public class UserService implements BeanNameAware, BeanPostProcessor {

    @Autowired
    private OrderService orderService;

    private String beanName;

    public void test() {
        System.out.println(orderService);
        System.out.println(beanName);
    }

    @Override
    public void setBeanName(String var1) {
        this.beanName = var1;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if ("userService".equals(beanName)) {
            System.out.println("前置postProcess执行===");
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
//        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {


        if ("userService".equals(beanName)) {
            System.out.println("后置postProcess执行===");
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
