package com.sourcecode.spring.bean;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/13 17:43
 * @Version 1.0.0
 * @Description
 **/
public class BeanDefinition {
    private Class clazz;
    private String scope;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
