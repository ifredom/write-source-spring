# spring source mini

## 步骤

整体步骤包含两部分内容：默认为spring框架内容，使用[应用]标注的表示：这一步骤是属于APP应用内部的

1. 创建管理容器 `SpringApplicationContext`：容器用于加载配置文件，并提供获取Bean的方法getBean()
2. (应用)创建一个应用配置类 `AppConfig`: 用于标识spring框架需要扫描的包路径
3. (应用)创建两个类 UserService,XxUtils：用于一会儿交给spring框架来扫描，一个是Bean对象，一个普通的类
4. 创建注解 `ComponentScan`,`Component`: 通过注解的方式来标记：哪些包需要被扫描，被扫描包目录下使用了 `@Component` 标记的类才就是Bean对象
5. 通过`类加载器`来`获取注解标记的所有类对象`,并准备将类对象转换为Bean对象的指定数据类型 `BeanDefinition`
6. 此时，我们将 Bean 设计为两种类型： `单例 singleton` ，`原型 prototype`. 因此创建一个注解 `@Scope` 用来标记这个对象是哪一种Bean类型
7. 在扫描过程中，将所有的Bean都以 <beanName,beanDefinition>的键值对存入 beanDefinitionObjects中管理。
8. 扫描完成后，将所有bean对象scope作用域为单例的创建对应实例
9. 
10. AOP载入

### 分布参考阅读

- [步骤6 中的scope](https://waylau.com/custom-scope-in-spring/#:~:text=%E5%A4%A7%E5%AE%B6%E5%AF%B9%E4%BA%8E%20Spring%20%E7%9A%84%20scope%20%E5%BA%94%E8%AF%A5%E9%83%BD%E4%B8%8D%E4%BC%9A%E9%BB%98%E8%AE%A4%E3%80%82%20%E6%89%80%E8%B0%93%20scope%EF%BC%8C%E5%AD%97%E9%9D%A2%E7%90%86%E8%A7%A3%E5%B0%B1%E6%98%AF%E2%80%9C%E4%BD%9C%E7%94%A8%E5%9F%9F%E2%80%9D%E3%80%81%E2%80%9C%E8%8C%83%E5%9B%B4%E2%80%9D%EF%BC%8C%E5%A6%82%E6%9E%9C%E4%B8%80%E4%B8%AA%20bean,%E7%9A%84%20scope%20%E9%85%8D%E7%BD%AE%E4%B8%BA%20singleton%EF%BC%8C%E5%88%99%E4%BB%8E%E5%AE%B9%E5%99%A8%E4%B8%AD%E8%8E%B7%E5%8F%96%20bean%20%E8%BF%94%E5%9B%9E%E7%9A%84%E5%AF%B9%E8%B1%A1%E9%83%BD%E6%98%AF%E7%9B%B8%E5%90%8C%E7%9A%84%EF%BC%9B%E5%A6%82%E6%9E%9C%20scope%20%E9%85%8D%E7%BD%AE%E4%B8%BAprototype%EF%BC%8C%E5%88%99%E6%AF%8F%E6%AC%A1%E8%BF%94%E5%9B%9E%E7%9A%84%E5%AF%B9%E8%B1%A1%E9%83%BD%E4%B8%8D%E5%90%8C%E3%80%82)
  . 对于scope的单例和原型类型，我们可以简单采用enum枚举类型，也可以尝试像spring源码中定义 `org.springframework.beans.factory.config.Scope`接口
- [java clazz.getDeclaredConstructor().newInstance() 和 class.newInstance() 的区别](https://blog.csdn.net/Adeluoo/article/details/124026775)