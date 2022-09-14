package com.sourcecode.content.service;

import com.sourcecode.spring.annotation.Autowired;
import com.sourcecode.spring.annotation.Component;
import com.sourcecode.spring.annotation.Scope;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/12 17:31
 * @Version 1.0.0
 * @Description
 **/
@Component("userService")
//@Scope("prototype")
public class UserService {

    @Autowired
    private OrderService orderService;

    public void test() {
        System.out.println(orderService);
    }
}
