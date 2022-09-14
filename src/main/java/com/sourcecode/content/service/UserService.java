package com.sourcecode.content.service;

import com.sourcecode.spring.annotation.Autowired;
import com.sourcecode.spring.annotation.Component;
import com.sourcecode.spring.annotation.Scope;
import com.sourcecode.spring.bean.BeanNameAware;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/12 17:31
 * @Version 1.0.0
 * @Description
 **/
@Component("userService")
@Scope("prototype")
public class UserService implements BeanNameAware {

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
}
