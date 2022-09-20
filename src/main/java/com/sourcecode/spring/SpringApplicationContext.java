package com.sourcecode.spring;

import com.sourcecode.spring.annotation.Autowired;
import com.sourcecode.spring.annotation.Component;
import com.sourcecode.spring.annotation.ComponentScan;
import com.sourcecode.spring.annotation.Scope;
import com.sourcecode.spring.bean.BeanDefinition;
import com.sourcecode.spring.bean.BeanNameAware;
import com.sourcecode.spring.bean.BeanPostProcessor;
import com.sourcecode.spring.bean.InitializingBean;
import com.sourcecode.spring.interfaces.ObjectFactory;
import com.sourcecode.spring.utils.Asset;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
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
     * 单例池 一级缓存：存放初始化完成的 bean
     **/
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /**
     * 二级缓存：存放原始的 bean 对象（尚未填充属性）
     **/
    private Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    /**
     * 三级级缓存：存放 bean 工厂对象，用于解决循环依赖
     **/
    private Map<String, ObjectFactory<Object>> singletonFactories = new HashMap<>(16);

    /**
     * bean 配置信息 Map
     */
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * 切面处理器
     **/
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    /**
     * 保存 处于正在创建中的状态的Bean的名称
     */
    private Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    public SpringApplicationContext(Class<?> primarySource, Class<?> configClass) {
        this.configClass = configClass;

        scanBeanDefinition(configClass);

        registerBeanPostProcessors();

        preInstantiateSingletons();
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
     * 初始化:实例化所有的单例Bean
     */
    private void preInstantiateSingletons() {
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (beanDefinition.isSingleton()) {
                getBean(beanName);
            }
        });
    }

    public Object getBean(String beanName) {
        Asset.notNull(beanName);
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NullPointerException("没有找到bean：" + beanName);
        } else {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

            if (beanDefinition.isSingleton()) {
                Object singletonObject = getSingleton(beanName, true);

                // 三级缓存中都没有，那么就只能 create
                if (singletonObject == null) {
                    singletonObject = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName, singletonObject);
                    earlySingletonObjects.remove(beanName);
                    singletonFactories.remove(beanName);
                }

                return singletonObject;
            } else {
                // prototype.每次创建新对象
                return createBean(beanName, beanDefinition);
            }
        }
    }

    /**
     * 依次尝试从 3 处缓存中取获取单例
     *
     * @param beanName bean名字
     * @return {@link Object}
     */
    private Object getSingleton(String beanName, boolean allowEarlyReference) {

        // 从1级获取
        Object singletonObject = this.singletonObjects.get(beanName);

        if (singletonObject == null && this.isSingletonCurrentlyInCreation(beanName)) {

            // 从2级获取
            singletonObject = this.earlySingletonObjects.get(beanName);

            if (singletonObject == null && allowEarlyReference) {

                // 从3级获取
                ObjectFactory<Object> singletonFactory = this.singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    this.singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }


    /**
     * 创建bean
     *
     * @param beanDefinition bean定义
     * @param beanName       bean名字
     * @return {@link Object}
     */
    public Object createBean(String beanName, final BeanDefinition beanDefinition) {

        // 创建中
        beforeCreation(beanName, beanDefinition);

        try {
            // 创建对象
            Object instance = createBeanInstance(beanDefinition);

            // 依赖注入前,将工厂对象存入三级缓存 singletonFactories 中
            boolean earlySingletonExposure = beanDefinition.isSingleton() && isSingletonCurrentlyInCreation(beanName);
            if (earlySingletonExposure) {
                //添加三级缓存
                Object exposedObject = instance;
                this.singletonFactories.put(beanName, () -> exposedObject);
                this.earlySingletonObjects.remove(beanName);
            }

            populateBean(beanDefinition, instance);

            instance = initializeBean(beanName, instance);

            return instance;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            // 创建结束
            afterCreation(beanName, beanDefinition);
        }
    }

    /**
     * 依赖注入
     * <p>
     * 填充bean，将bean中使用 Autowired 标记的属性进行赋值
     *
     * @param beanDefinition bean配置
     * @param instance       实例
     * @throws IllegalAccessException 非法访问异常
     */
    private void populateBean(BeanDefinition beanDefinition, Object instance) throws IllegalAccessException, InvocationTargetException {
        Class<?> clazz = beanDefinition.getClazz();
        // 解析字段上的 Autowired
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                field.set(instance, field.getName());
            }
        }
        // 解析方法上的 Autowired
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Autowired.class)) {
                // 编译时加上 -parameters 参数才能反射获取到参数名
                // 或者编译时加上 -g 参数，使用 ASM 获取到参数名
                String paramName = method.getParameters()[0].getName();

                method.invoke(instance, paramName);
            }
        }
    }

    protected void addSingleton(String beanName, Object singletonObject) {

    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {

    }

    /**
     * 扫描.解析配置类
     * 过程：通过传入的配置信息 ComponentScan 获取扫描路径  --> 执行扫描 @Component
     *
     * @param configClass 配置类
     */
    private void scanBeanDefinition(Class<?> configClass) {

        // 1. 传递来的类，是都被扫描注解标记
        ComponentScan componentScanAnnotation = configClass.getAnnotation(ComponentScan.class);
        String scanPath = componentScanAnnotation.value();

        // 2. 获取扫扫描路径后，准备扫描。一个包下有许多的类，我们框架关心的是被指定注解标记的类（@Component），才会被扫描
        // 如何获取一个包下面的java类？使用类加载器

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
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


                            // 2.2 使用 @Component 注解装饰类：就表示希望将它交给Spring容器托管，它是一个bean对象
                            //  class  ---??--->  Bean
                            // 2.3 在将class转换为我们制定的Bean类型时，由于Bean有两种类型：单例和原型。需要使用单例模式来确保Bean对象的唯一性
                            // 因此，如何实现单例呢？
                            // 可以创建一个@Scope注解来标识它是单例还是原型Bean类型，同时创建一个Map来保存所有的Bean。
                            // Map {BeanName,BeanObject}  也就是常说的单例池

                            // 2.4 判断是单例Bean还是原型Bean
                            Component componentAnnotation = aClass.getDeclaredAnnotation(Component.class);
                            String beanName = componentAnnotation.value();


                            if ("".equals(beanName)) {
                                beanName = Introspector.decapitalize(aClass.getSimpleName());
                            }


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

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    /**
     * 注册 Bean前/后置处理器
     * 由视频中的位置提取了出来
     */
    private void registerBeanPostProcessors() {
        // 初始化前，储存实现了前置处理器的Bean对象
        this.beanDefinitionMap.entrySet()
                .stream()
                .filter((entry) -> BeanPostProcessor.class.isAssignableFrom(entry.getValue().getClazz()))
                .forEach((entry) -> {
                    BeanPostProcessor beanPostProcessor = (BeanPostProcessor) getBean(entry.getKey());
                    beanPostProcessorList.add(beanPostProcessor);
                });

        // 原写法
//        if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
//            BeanPostProcessor instance = (BeanPostProcessor) aClass.getDeclaredConstructor().newInstance();
//            beanPostProcessorList.add(instance);
//        }
    }


    /**
     * 初始化bean
     *
     * @param beanName bean名字
     * @param bean 实例
     * @return {@link Object}
     */
    private Object initializeBean(String beanName, Object bean) throws Exception {
        // 0️⃣ 容器感知：各种 Aware 回调
        if (bean instanceof BeanNameAware) {
            ((BeanNameAware) bean).setBeanName(beanName);
        }

        // 1️⃣ 初始化前，调用前置处理器（Hook）
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
        }

        // 2️⃣ 初始化
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }

        // 3️⃣ 初始化后，调用后置处理器（Hook）
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
        }

        return bean;
    }


    /**
     * 创建bean实例
     * <p>
     * !支持无参构造 和 有参构造
     *
     * @param beanDefinition bean定义
     * @return {@link Object}
     * @throws NoSuchMethodException     没有这样方法异常
     * @throws InvocationTargetException 调用目标异常
     * @throws InstantiationException    实例化异常
     * @throws IllegalAccessException    非法访问异常
     */
    private Object createBeanInstance(BeanDefinition beanDefinition) throws Throwable {
        Class<?> clazz = beanDefinition.getClazz();

        Constructor<?>[] constructors = clazz.getConstructors();

        // 默认使用无参构造函数，创建实例对象
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return constructor.newInstance();
            }
        }

        // 有参构造函数(Map结构无序，所以是随机得)
        Constructor<?> constructor = constructors[0];
        Object[] args = new Object[constructor.getParameterCount()];
        Parameter[] parameters = constructor.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = null;


            // 被依赖注入标记得属性，从 beanDefinitionMap中获取
            if (parameter.getClass().isAnnotationPresent(Autowired.class)) {
                arg = getBean(parameter.getName());
            }
            args[i] = arg;
        }
        return constructor.newInstance(args);
    }

    private void beforeCreation(String beanName, BeanDefinition beanDefinition) {
        if (beanDefinition.isSingleton()) {
            beforeSingletonCreation(beanName);
        }
    }

    private void afterCreation(String beanName, BeanDefinition beanDefinition) {
        if (beanDefinition.isSingleton()) {
            afterSingletonCreation(beanName);
        }
    }

    private void beforeSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.add(beanName)) {
            throw new IllegalStateException("bean" + beanName + "正在创建中，是否存在无法解析的循环引用");
        }
    }

    private void afterSingletonCreation(String beanName) {
        this.singletonsCurrentlyInCreation.remove(beanName);
    }

    protected Boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
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
