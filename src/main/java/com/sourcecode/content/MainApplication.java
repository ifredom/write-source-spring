package com.sourcecode.content;

import com.sourcecode.content.service.UserService;
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

        SpringApplicationContext app = SpringApplicationContext.run(MainApplication.class, AppConfig.class);

        UserService userService = (UserService) app.getBean("userService");

        userService.test();

        System.out.println("=================");

    }
}
