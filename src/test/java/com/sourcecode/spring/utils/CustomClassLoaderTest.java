package com.sourcecode.spring.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class CustomClassLoaderTest {

    private CustomClassLoader customClassLoaderUnderTest;

    @Test
    @DisplayName("测试 ClassLoader 层级关系")
    void testLevelRelation() {
        customClassLoaderUnderTest = new CustomClassLoader("com.sourcecode.content.service");

        ClassLoader classLoader = CustomClassLoaderTest.class.getClassLoader();

        ClassLoader classLoaderParent = classLoader.getParent();

        ClassLoader classLoader1 = classLoaderParent.getParent();


        System.out.println(classLoader);
        System.out.println(classLoaderParent);
        System.out.println(classLoader1);
    }

    @Test
    @DisplayName("测试 自定义类加载调用 ")
    void testCustomClassLoader() {
        //创建自定义classloader对象。
        CustomClassLoader diskLoader = new CustomClassLoader("D:\\lib");
        try {
            //加载class文件
            Class c = diskLoader.loadClass("com.sourcecode.content.service");

            if(c != null){
                try {
                    Object obj = c.newInstance();
                    Method method = c.getDeclaredMethod("say",null);
                    //通过反射调用Test类的say方法
                    method.invoke(obj, null);
                } catch (InstantiationException | IllegalAccessException
                        | NoSuchMethodException
                        | SecurityException |
                        IllegalArgumentException |
                        InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
