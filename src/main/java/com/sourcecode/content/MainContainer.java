package com.sourcecode.content;

import com.sourcecode.spring.SpringApplicationContext;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/12 17:15
 * @Version 1.0.0
 * @Description
 **/
public class MainContainer {
    public static void main(String[] args) {
        AppConfig springConfig = new AppConfig();
        SpringApplicationContext app = new SpringApplicationContext(AppConfig.class);

        Object userService = app.getBean("userService");
        System.out.println(userService);
    }
}
