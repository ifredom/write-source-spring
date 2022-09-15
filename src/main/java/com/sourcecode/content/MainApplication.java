package com.sourcecode.content;

import com.sourcecode.content.service.UserService;
import com.sourcecode.content.service.UserServiceImpl;
import com.sourcecode.spring.SpringApplicationContext;
import com.sourcecode.spring.bean.BeanNameAware;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/12 17:15
 * @Version 1.0.0
 * @Description
 **/
public class MainApplication {
    public static void main(String[] args) {
        AppConfig springConfig = new AppConfig();

        SpringApplicationContext app = SpringApplicationContext.run(MainApplication.class, AppConfig.class);

        Object userService = app.getBean("userService");
        UserService userService1 = (UserService) app.getBean("userService");
        UserService userService2 = (UserService) app.getBean("userService");

        System.out.println(userService);
        System.out.println(userService1);
        System.out.println(userService2);

        userService1.test();

        System.out.println("=================");



    }
}
