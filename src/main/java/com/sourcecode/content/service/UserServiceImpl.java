package com.sourcecode.content.service;

import com.sourcecode.spring.annotation.Autowired;
import com.sourcecode.spring.annotation.Component;
import com.sourcecode.spring.annotation.Scope;
import com.sourcecode.spring.bean.BeanNameAware;
import com.sourcecode.spring.bean.BeanPostProcessor;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/12 17:31
 * @Version 1.0.0
 * @Description
 **/
@Component("userService")
@Scope("prototype")
public class UserServiceImpl implements UserService, BeanNameAware, BeanPostProcessor {

    @Autowired
    private OrderService orderService;

    private String beanName;

    @Override
    public void test() {
        System.out.println("进入 test 方法体 开始执行");
        System.out.println(orderService);
        System.out.println(beanName);
        System.out.println("进入 test 方法体 结束执行");
    }

    @Override
    public void setBeanName(String var1) {
        this.beanName = var1;
    }
}
