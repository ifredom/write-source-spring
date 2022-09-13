package com.sourcecode.spring;

import com.sourcecode.spring.annotation.Component;
import com.sourcecode.spring.annotation.ComponentScan;
import com.sourcecode.spring.annotation.Scope;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * 单例池
     */
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public SpringApplicationContext(Class configClass) throws ClassNotFoundException {
        this.configClass = configClass;

        // 解析配置类
        // 过程：通过传入的配置信息 ComponentScan 获取扫描路径  --> 执行扫描 @Component

        // 1. 传递来的类，是都被扫描注解标记
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String scanPath = componentScanAnnotation.value();
        System.out.println(scanPath);

        // 2. 获取扫扫描路径后，准备扫描。一个包下有许多的类，我们框架关心的是被指定注解标记的类（@Component），才会被扫描
        // 如何获取一个包下面的java类，这个就是类加载器

        ClassLoader classLoader = SpringApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource("com/sourcecode/content/service");
        assert resource != null;
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            for (File listFile : Objects.requireNonNull(file.listFiles())) {

                // listFile.getAbsolutePath()  获取的是操作系统中的完整绝对路径
                // 2.1 由于 classLoader.loadClass(quotePath) 需要获取的是 com.xxx.xxx这样的引用地址，所以需要转换一下
                String absolutePath = listFile.getAbsolutePath();
                if (absolutePath.endsWith(".class")) {
                    String quotePath = resolveClassAbsolutePath(absolutePath);
                    try {
                        Class<?> aClass = classLoader.loadClass(quotePath);

                        if (aClass.isAnnotationPresent(Component.class)) {
                            // 2.2 使用 @Component 注解装饰类：就表示希望将它交给Spring容器托管，它是一个bean对象
                            //  class  ---??--->  Bean
                            // 2.3 在将class转换为我们制定的Bean类型时，由于Bean有两种类型：单例和原型。需要使用单例模式来确保Bean对象的唯一性
                            // 因此，如何实现单例呢？ 可以创建一个@Scope注解来标识它是单例还是原型Bean类型，同时创建一个Map来保存所有的Bean。
                            // Map {BeanName,BeanObject}  也就是常说的单例池

                            // 2.4 判断是单例Bean还是原型Bean
                            Scope scopeAnnotation = aClass.getAnnotation(Scope.class);
                            System.out.println(scopeAnnotation);
                            System.out.println(scopeAnnotation.value());

                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    public Object getBean(String beanName) {
        return null;
    }

    /**
     * 得到类名
     * 将 : D:\project-myself\a-java\write-source-spring\target\classes\com\sourcecode\content\service\UserService.class
     * 转换为: com.sourcecode.content.service.UserService
     *
     * @param classPath 类路径
     * @return {@link String}
     */
    public String resolveClassAbsolutePath(String classPath) {

        return classPath.substring(classPath.indexOf("com"), classPath.indexOf(".class")).replace("\\", ".");
    }
}
