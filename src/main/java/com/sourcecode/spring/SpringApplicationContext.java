package com.sourcecode.spring;

import com.sourcecode.spring.annotation.Autowired;
import com.sourcecode.spring.annotation.Component;
import com.sourcecode.spring.annotation.ComponentScan;
import com.sourcecode.spring.annotation.Scope;
import com.sourcecode.spring.bean.BeanDefinition;
import com.sourcecode.spring.bean.BeanNameAware;
import com.sourcecode.spring.bean.BeanPostProcessor;
import com.sourcecode.spring.bean.InitializingBean;
import com.sourcecode.spring.utils.Asset;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    private static SpringApplicationContext SpringApplication;

    /**
     * 配置类
     */
    private Class<?> configClass;

    /**
     * 单例池
     */
    private ConcurrentHashMap<String, Object> singletonMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public SpringApplicationContext(Class<?> primarySource, Class<?> configClass) {
        this.configClass = configClass;

        scan(configClass);

        // 实例化所有的单例Bean
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if ("singleton".equals(beanDefinition.getScope())) {
                Object beanInstance = createBean(beanName, beanDefinition);
                singletonMap.put(beanName, beanInstance);
            }
        }

    }

    /**
     * 仿照spring的静态run方法
     * spring源码中是通过静态run方法调用实例run方法。本demo简化了
     *
     * @param primarySource 主要来源
     * @param configClass   配置类
     * @param args          arg参数
     * @return {@link Object}
     */
    public static SpringApplicationContext run(Class<?> primarySource, Class<?> configClass, String... args) {
        return new SpringApplicationContext(primarySource, configClass);
    }

    /**
     * 创建bean
     *
     * @param beanDefinition bean定义
     * @return {@link Object}
     */
    public Object createBean(String beanName, final BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getClazz();
        Object instance = null;

        try {
            instance = clazz.getDeclaredConstructor().newInstance();

            // 依赖注入 实现
            // 将bean中使用 Autowired 标记的属性进行赋值

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Object bean = getBean(field.getName());
                    field.setAccessible(true);
                    field.set(instance, bean);
                }
            }

            // 实现BeanName 感知
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            // 初始化前，调用前置处理器（Hook）
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            // 初始化
            if (instance instanceof InitializingBean) {
                try {
                    ((InitializingBean) instance).afterPropertiesSet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 初始化后，调用后置处理器（Hook）
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return instance;
    }

    /**
     * 扫描.解析配置类
     * 过程：通过传入的配置信息 ComponentScan 获取扫描路径  --> 执行扫描 @Component
     *
     * @param configClass 配置类
     */
    private void scan(Class<?> configClass) {

        // 1. 传递来的类，是都被扫描注解标记
        ComponentScan componentScanAnnotation = configClass.getAnnotation(ComponentScan.class);
        String scanPath = componentScanAnnotation.value();

        // 2. 获取扫扫描路径后，准备扫描。一个包下有许多的类，我们框架关心的是被指定注解标记的类（@Component），才会被扫描
        // 如何获取一个包下面的java类？使用类加载器

        ClassLoader classLoader = SpringApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource("com/sourcecode/content/service");
        assert resource != null;
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            for (File listFile : Objects.requireNonNull(file.listFiles())) {

                // 2.1 由于 classLoader.loadClass(quotePath) 需要获取的是 com.xxx.xxx这样的引用地址，所以需要转换一下
                String absolutePath = listFile.getAbsolutePath();
                if (absolutePath.endsWith(".class")) {
                    String quotePath = resolveClassAbsolutePath(absolutePath);
                    try {
                        Class<?> aClass = classLoader.loadClass(quotePath);

                        if (aClass.isAnnotationPresent(Component.class)) {

                            // 初始化前，储存实现了前置处理器的Bean对象
                            if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
                                BeanPostProcessor instance = (BeanPostProcessor) aClass.getDeclaredConstructor().newInstance();
                                beanPostProcessorList.add(instance);
                            }


                            // 2.2 使用 @Component 注解装饰类：就表示希望将它交给Spring容器托管，它是一个bean对象
                            //  class  ---??--->  Bean
                            // 2.3 在将class转换为我们制定的Bean类型时，由于Bean有两种类型：单例和原型。需要使用单例模式来确保Bean对象的唯一性
                            // 因此，如何实现单例呢？
                            // 可以创建一个@Scope注解来标识它是单例还是原型Bean类型，同时创建一个Map来保存所有的Bean。
                            // Map {BeanName,BeanObject}  也就是常说的单例池

                            // 2.4 判断是单例Bean还是原型Bean
                            Component componentAnnotation = aClass.getDeclaredAnnotation(Component.class);
                            String beanName = componentAnnotation.value();

                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(aClass);

                            if (aClass.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = aClass.getDeclaredAnnotation((Scope.class));
                                String scope = scopeAnnotation.value();
                                beanDefinition.setScope(scope);
                            } else {
                                beanDefinition.setScope("singleton");
                            }
                            // 存储Bean
                            beanDefinitionMap.put(beanName, beanDefinition);

                        }

                    } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public Object getBean(String beanName) {
        Asset.notNull(beanName);
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

            String scope = beanDefinition.getScope();
            if ("singleton".equals(scope)) {
                return singletonMap.get(beanName);
            } else {
                // 创建一个新Bean对象
                return createBean(beanName, beanDefinition);
            }

        } else {
            // 没有这个beanName
            throw new NullPointerException();
        }
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
