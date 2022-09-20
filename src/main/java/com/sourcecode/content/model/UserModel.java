package com.sourcecode.content.model;

import java.io.Serializable;

/**
 * @Author ifredomvip@gmail.com
 * @Date 2022/9/20 10:46
 * @Version 1.0.0
 * @Description
 **/
public class UserModel implements Serializable {

    private String name;
    private Integer age;

    public UserModel() {
    }

    public UserModel(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
