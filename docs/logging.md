---
title: 日志系统
---

# <H2Icon />日志系统

lin-cms的日志系统基于spring-boot和logback，在此之上提供了日志记录文件和请求
日志记录两个功能。

## 使用

lin-cms推荐使用lombok注解的方式去记录日志。

首先，请在需要进行日志记录的类上打上`Slf4j`注解，如下：

```java
@Slf4j
public class RequestLogInterceptor extends HandlerInterceptorAdapter {
  // 省略
}
```

当开启该注解后，便可以在类中方便的使用`log`进行日志记录：

```
log.info("[{}] -> [{}] from: {} costs: {}ms",
                request.getMethod(),
                request.getServletPath(),
                request.getRemoteAddr(),
                System.currentTimeMillis() - startTime.get()
 );
```

logback日志共有五个等级，分别为`trace`，`debug`，`info`，`warn`和`error`，即：

```
log.trace();
log.info();
log.warn();
log.debug();
log.error();
```

请根据实际的日志等级调用正确的方法。

lin-cms仅在spring-boot和logback的基础上，增加了一些必要的日志功能，因此保留了
spring-boot的日志配置方式，如果你不熟悉spring-boot可以查阅一下它的文档。

## 日志记录

lin-cms将日志会记录到终端和文件两个地方，在生产环境下只会向文件中记录。
记录日志的文件默认在工作目录下的`log`文件夹中，如下：

```bash
logs
└── 2020-01
  ├── 2020-01-06.log
  ├── 2020-01-09.log
  └── 2020-01-13.log
```

文件以一个月作为一个子目录，每一个子目录下皆有每一天的日志文件。
当某一天日志文件超过一定的大小时，会被切割，默认的切割大小为`5M`。

当然你也可以通过修改resources目录下的`logback-spring.xml`文件来修改日志的记录方式。

logback-spring.xml文件中的`log.path`属性可以指定日志文件的记录位置，如下：

```xml
<property name="log.path" value="logs/"/>
```

如果需要改变日志文件的存储位置，可以修改该属性（可以为绝对路径）达到你的目的。


## 日志配置

`logback-spring.xml`文件是logback默认配置文件，你可以更换`appender`，修改
日志文件分割大小。其中几个重要的配置如下：

```
<property name="log.path" value="logs/"/>    // 日志存储文件
```

```
<appender name="FILE" class="com.lin.cms.core.logger.AdvanceRollingFileAppender">  // 自定义日志appender
    <dir>${log.path}</dir> // 日志存储位置
    <maxFileSize>5MB</maxFileSize> // 日志切割大小，默认为5M
    <encoder> // 日志记录格式
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        <charset>UTF-8</charset> // 文件默认编码
    </encoder>
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
        <level>debug</level> // 日志过滤等级，默认为debug，即记录debug以上的等级
    </filter>
</appender>
```

logback中，日志等级是一个较为重要的概念，`trace`，`debug`，`info`，`warn`和`error`的等级依次递增，
只要日志等级足够才会被记录，如上面的配置文件中指定了日志过滤等级为`debug`，则在`debug`之上的
等级日志会被记录，所以`trace`日志不会被记录。

大多数情况下，你只需要修改上面提到的几个配置就足够了，如果你需要定制自己的日志格式和
记录方式，可以查阅logback文档。

一般情况下，我们都推荐通过`application.properties`配置文件来改变日志记录等级，如下：

```properties
# 日志等级
logging.level.com.lin.cms.demo.mapper=debug
logging.level.web=debug
# 日志配置文件
logging.config=classpath:logback-spring.xml
# 是否开启请求日志记录
request-log.enabled=true
```

`logging.level`可以指定日志记录等级，`com.lin.cms.demo.mapper`是包名，表示这个包下的所有
日志记录等级均为`debug`，你也可以采用这种方式来指定特定包的日志等级。

`request-log.enabled`表示是否开启请求日志记录，默认是开，当然如果你不需要也可以关闭。


:::tip

如果你使用lin-cms，我们还是建议你直接使用我们的日志模式，在application.properties配置相应包的
日志记录等级其实已经足够了。

:::

<RightMenu />