package com.sourcecode.spring.exception;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/14 17:36
 * @Version 1.0.0
 * @Description
 **/
public class BeansException extends RuntimeException{
    private String msg;

    public BeansException(String msg) {
        this.msg = msg;
    }
}
