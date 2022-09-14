package com.sourcecode.content;

import com.sourcecode.spring.SpringApplicationContext;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/12 17:15
 * @Version 1.0.0
 * @Description
 **/
public class MainApplication {
    public static void main(String[] args) {
        AppConfig springConfig = new AppConfig();

        SpringApplicationContext app = null;
        app = new SpringApplicationContext(AppConfig.class);

        Object userService = app.getBean("userService");
        Object userService1 = app.getBean("userService");
        Object userService2 = app.getBean("userService");

        System.out.println(userService);
        System.out.println(userService1);
        System.out.println(userService2);
    }
}
