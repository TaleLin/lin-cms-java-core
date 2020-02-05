---
title: 起步
---

# 起步

本小节将以最快、最便捷的方式教你如何启动 lin-cms 。

## 依赖

- JDK1.8+，已在JAVA8、9、11上测试通过。
- MySQL5.6+，确保你有可用的数据库环境。
- Maven3.6+，依赖、打包需要它。

lin-cms 依赖于 spring-boot 和 mybatis，你如果不满足看一看，那么请一定要
有一定的基础。

## 获取项目

```bash
git clone https://github.com/TaleLin/lin-cms-java.git merak
```

> 此处我们以 merak 作为工程名，当然你也可以以任意你喜爱的名字作为工程名。如果 你想以某个版本，如 0.0.1 版，作为起始项目，那么请在 github 上的版本页下载相应 的版本即可。

## 安装依赖

进入项目目录：

```bash
cd merak
```

安装依赖并打包 jar 包

```bash
mvn install -Dmaven.test.skip=true 
```

## 数据库配置

### 导入数据

在你的开发环境 RDBMS 中，新建一个数据库，如 `lin-cms`。

然后找到目录下的`/src/main/resources/schema.sql`文件，并在 MySQL 中执行该脚本文件。

推荐你使用 navicat 等数据库工具导入并执行脚本文件，如果你熟悉 mysql 客户端工具，也可使用它导入数据。

### 更改配置

找到`/src/main/resources/application-dev.properties`配置文件，并在其中修改你开发环境的数据库配置：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lin-cms?useSSL=false&serverTimezone=UTC&characterEncoding=UTF8
spring.datasource.username=root
spring.datasource.password=123456
``` 

## 运行

```bash
java -jar target/merak-0.0.1-SNAPSHOT.jar
```

## 完成

运行成功后，打开浏览器访问 `http://localhost:5000/` ，你可以看到 ：

**心上无垢，林间有风** 

几个大字，证明你一切已经 ok , 你可以进入后面的章节学习了。

当然你也可以通过其它的工具验证，如:

```bash
curl http://localhost:5000/
```

最后，祝贺你，开始了一段新的旅程。