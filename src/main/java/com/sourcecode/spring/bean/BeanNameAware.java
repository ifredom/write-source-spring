package com.sourcecode.spring.bean;

/**
 * 为bean使用者提供bean的名称
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/14 15:29
 * @Version 1.0.0
 * @Description
 **/
public interface BeanNameAware {
    /**
     * 设置bean名称
     *
     * @param var1 var1
     */
    void setBeanName(String var1);
}
