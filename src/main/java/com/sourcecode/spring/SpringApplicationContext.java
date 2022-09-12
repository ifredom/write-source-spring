package com.sourcecode.spring;

/**
 * 容器类
 *
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/12 17:10
 * @Version 1.0.0
 * @Description
 **/
public class SpringApplicationContext {

    private Class configClass;

    public SpringApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 解析配置类
        // ComponentScan -> 获取扫描路径  --> 执行扫描

    }

    public Object getBean(String beanName){
        return null;
    }
}
