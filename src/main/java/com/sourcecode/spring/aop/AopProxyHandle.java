package com.sourcecode.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理通用模板
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/15 15:03
 * @Version 1.0.0
 * @Description
 **/
public class AopProxyHandle implements InvocationHandler {

    private Object target;

    public AopProxyHandle(Object target) {
        this.target = target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object gerProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(target, args);
        after();
        return result;
    }

    private void before() {
//        System.out.println("proxy action before...");
    }

    private void after() {
//        System.out.println("proxy action after...");
    }
}
