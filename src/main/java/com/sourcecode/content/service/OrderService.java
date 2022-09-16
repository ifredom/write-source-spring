package com.sourcecode.content.service;

import com.sourcecode.spring.annotation.Autowired;
import com.sourcecode.spring.annotation.Component;
import com.sourcecode.spring.annotation.Scope;
import com.sourcecode.spring.bean.InitializingBean;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/14 14:03
 * @Version 1.0.0
 * @Description
 **/
@Component("orderService")
//@Scope("prototype")
public class OrderService implements InitializingBean {

    private String var1;

//    @Autowired
//    private UserService userService;


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化  === afterPropertiesSet ===");
    }
}
