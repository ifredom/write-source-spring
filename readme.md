# spring source mini

## 步骤

1. 创建管理容器 `SpringApplicationContext`：加载配置文件，getBean()
2. 创建配置文件读取工具 `AppConfig`: 将从配置文件中读取的数据转换为spring指定结构
3. 创建注解 `ComponentScan`,`Component`: 用于通过注解使用方式来扫描加载Bean对象
4. 将配置文件数据中获取的bean装入容器并托管
5. AOP