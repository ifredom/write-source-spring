package com.sourcecode.spring.enums;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/13 17:31
 * @Version 1.0.0
 * @Description
 **/
public enum ScopeEnum {
    /**
     * εδΎ
     */
    singleton(1),
    /**
     * εε
     */
    prototype(2);


    private final int value;


    ScopeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
