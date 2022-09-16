package com.sourcecode.spring.interfaces;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/16 16:18
 * @Version 1.0.0
 * @Description
 **/
@FunctionalInterface
public interface ObjectFactory<T> {
    /**
     * 得到对象
     *
     * @return {@link T}
     * @throws RuntimeException 运行时异常
     */
    T getObject() throws RuntimeException;
}
