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
public class OrderService{

    private String var1;
    private String var2;

    @Autowired
    private UserService userService;

    public OrderService(String var1) {
        this.var1 = var1;
    }

    public OrderService(String var1, String var2) {
        this.var1 = var1;
        this.var2 = var2;
    }


    @Override
    public String toString() {
        return "OrderService{" +
                "var1='" + var1 + '\'' +
                ", var2='" + var2 + '\'' +
                '}';
    }
}
