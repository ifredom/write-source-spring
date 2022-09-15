# spring source mini

- [实现源码]()
- [视频](https://www.bilibili.com/video/BV1r5411A7hZ)

## 步骤

整体步骤包含两部分内容：默认为spring框架内容，使用[应用]标注的表示：这一步骤是属于APP应用内部的

1. 创建管理容器 `SpringApplicationContext`：容器用于加载配置文件，并提供获取Bean的方法getBean()
2. (应用)创建一个应用配置类 `AppConfig`: 用于标识spring框架需要扫描的包路径
3. (应用)创建两个类 UserService,XxUtils：用于一会儿交给spring框架来扫描，一个是Bean对象，一个普通的类
4. 创建注解 `ComponentScan`,`Component`: 通过注解的方式来标记：哪些包需要被扫描，被扫描包目录下使用了 `@Component` 标记的类才就是Bean对象
5. 通过`类加载器`来`获取注解标记的所有类对象`,并准备将类对象转换为Bean对象的指定数据类型 `BeanDefinition`
6. 此时，我们将 Bean 设计为两种类型： `单例 singleton` ，`原型 prototype`. 因此创建一个注解 `@Scope` 用来标记这个对象是哪一种Bean类型
7. 在扫描过程中，将所有的Bean都以 <beanName,beanDefinition>的键值对存入 `beanDefinitionMap` 中管理。
8. 扫描完成后，为所有bean对象创建对应实例。这里：scope作用域为单例的Bean首先在`beanDefinitionMap`中查找是否已创建，没有则创建，有则返回已创建的，scope作用域为原型的Bean直接创建对应实例

---
9. 依赖注入实现。所有的Bean已被扫描并存储在`beanDefinitionMap`中，并为所有的单例Bean创建了实例，所有的原型Bean仅仅是存储了其Bean数据结构`beanDefinition`
10. 容器感知beanNameAware： 对于某一个Bean本身(userService)而言，使用者是不知道关于容器的信息。实现 beanNameAware容器感知
11. Bean初始化方式: 在spring源码中提供了两种Bean初始化方式，这里实现其中之一 `initializingBean`
12. AOP实现：通过JAVA-JDK 动态代理实现,即实现AOP切面，实现位置位于 `BeanPostProcessor`.需要注意 代理是对 某一个具体对象的某种行为进行代理，所以必须被代理的对象必须至少实现一个接口

## 困难与挑战

在书写源码的过程中，遇到不清晰的知识点，记录在文件夹 `source-ex` 中


### 分布参考阅读

- [步骤6 中的scope](https://waylau.com/custom-scope-in-spring/#:~:text=%E5%A4%A7%E5%AE%B6%E5%AF%B9%E4%BA%8E%20Spring%20%E7%9A%84%20scope%20%E5%BA%94%E8%AF%A5%E9%83%BD%E4%B8%8D%E4%BC%9A%E9%BB%98%E8%AE%A4%E3%80%82%20%E6%89%80%E8%B0%93%20scope%EF%BC%8C%E5%AD%97%E9%9D%A2%E7%90%86%E8%A7%A3%E5%B0%B1%E6%98%AF%E2%80%9C%E4%BD%9C%E7%94%A8%E5%9F%9F%E2%80%9D%E3%80%81%E2%80%9C%E8%8C%83%E5%9B%B4%E2%80%9D%EF%BC%8C%E5%A6%82%E6%9E%9C%E4%B8%80%E4%B8%AA%20bean,%E7%9A%84%20scope%20%E9%85%8D%E7%BD%AE%E4%B8%BA%20singleton%EF%BC%8C%E5%88%99%E4%BB%8E%E5%AE%B9%E5%99%A8%E4%B8%AD%E8%8E%B7%E5%8F%96%20bean%20%E8%BF%94%E5%9B%9E%E7%9A%84%E5%AF%B9%E8%B1%A1%E9%83%BD%E6%98%AF%E7%9B%B8%E5%90%8C%E7%9A%84%EF%BC%9B%E5%A6%82%E6%9E%9C%20scope%20%E9%85%8D%E7%BD%AE%E4%B8%BAprototype%EF%BC%8C%E5%88%99%E6%AF%8F%E6%AC%A1%E8%BF%94%E5%9B%9E%E7%9A%84%E5%AF%B9%E8%B1%A1%E9%83%BD%E4%B8%8D%E5%90%8C%E3%80%82)
  . 对于scope的单例和原型类型，我们可以简单采用enum枚举类型，也可以尝试像spring源码中定义 `org.springframework.beans.factory.config.Scope`接口
- [步骤8 创建Bean实例 java clazz.getDeclaredConstructor().newInstance() 和 class.newInstance() 的区别](https://blog.csdn.net/Adeluoo/article/details/124026775)
- [步骤10 InitializingBean作用](https://www.cnblogs.com/liaowenhui/p/16676819.html#:~:text=Initiali,%E9%83%BD%E4%BC%9A%E6%89%A7%E8%A1%8C%E8%AF%A5%E6%96%B9%E6%B3%95%E3%80%82)
- [步骤12 jdk-动态代理](https://zhuanlan.zhihu.com/p/347141071)
- [其他源码实现](https://github.com/mafei007even/Spring-impl/blob/06ab6fdf762c7506e65068d3fe18c0e09c539a79/src/com/mafei/spring/MaFeiApplicationContext.java#L153)