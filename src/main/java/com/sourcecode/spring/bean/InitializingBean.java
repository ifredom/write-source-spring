package com.sourcecode.spring.bean;

/**
 * 属性初始化
 *
 * 等同于 <bean id="exampleInitBean" class="examples.ExampleBean" init-method="init"/>
 *
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/14 16:17
 * @Version 1.0.0
 * @Description
 **/
public interface InitializingBean {
    /**
     * 属性后置调用
     *
     * @throws Exception 异常
     */
    void afterPropertiesSet() throws Exception;
}
