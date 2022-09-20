package com.sourcecode.content.service;

import com.sourcecode.content.model.UserModel;
import com.sourcecode.spring.bean.InitializingBean;

/**
 * 初始化Bean对象
 *
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/19 17:16
 * @Version 1.0.0
 * @Description
 **/
public class WalletService implements InitializingBean {

    private UserModel user;

    public void setUser(UserModel user) {
        this.user = user;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setName("ifredom");
        userModel.setAge(18);

        this.user = userModel;
    }
}
