---
title: 扩展
---

# <H2Icon />扩展

## 概述

小皮同学接到了一个开发任务：开发一个基于zookeeper的配置中心。熟悉zookeeper
的小皮信心满满，两三天就将这个功能开发完毕，单测、文档均已搞定，于是汇报给了
leader。

leader 很是满意，不过前方运维传来了不幸的消息，zookeeper集群出现重大问题，
不能再分配相应机器供配置调用。没有办法，只能安排小皮使用公司的etcd集群重新
开发配置中心了。

小皮很是难过，只能硬着头皮进行魔改。重构的时候，小皮发现原来的代码严重耦合，
虽然业务完成了，但不易维护和添加新特性。小皮苦思良久，又花了一个星期的时间
将配置中心完全重构，现在配置中心不仅支持etcd和zookeeper，而且还能一键切换
，leader很是高兴，奖励了小皮。

对于一个业务接口，不同的具体实现，支持一键切换，这样的开发模式是解决复杂业务，
保持系统稳定的一个重要解决方案。

lin-cms将这样的功能点单独抽象出了一个概念——`扩展`。扩展应具有以下几种特点：

1. 一种功能，多种实现。
2. 独立于项目，不依赖项目的业务。
3. 实现替换简单，开箱即用


## 实例

我们还是以`file`作为例子来详细的讲解扩展的使用，文件扩展的代码目录结构如下：

```bash
├── File.java
├── FileConsts.java
├── FileProperties.java
├── FileUtil.java
├── LocalUploader.java
├── PreHandler.java
├── Uploader.java
└── config.properties
```

首先，你可以将扩展理解为一个业务点，既然是业务点，那么一定会对外提供业务支持；
没错，`file`对外提供`Uploader`的业务支持，即文件上传的业务支持。

`Uploader`是一个接口，同时它也是一个出口，一个提供文件上传服务的出口。而其它
文件都是服务于`Uploader`的，如`File`是上传文件信息的数据容器，`FileUtil`则是
基础的工具类。

既然`Uploader`是一个服务接口，那么它必然有服务使用者，目前这个服务的使用者是
业务层中的`FileService`。

好了，我们回到正题，继续来谈代码结构；FileConsts是字符串常量相关类，方便字符串的
维护；FileProperties是文件上传的相关配置类，它与`config.properties`搭配，一个提供
配置文件，一个提供程序配置类；LocalUploader是`Uploader`的具体实现，它是第一个默认实现，
你也可以提供相关的实现来满足你自己的业务。PreHandler非必须，只是`Uploader`业务实现的
一个点。

谈到这里，我们来总结一下扩展：

1. 扩展其实就是一个微型接口，它有自己独立的配置，独立的逻辑和实现。
2. 扩展必须独立，它不应该依赖工程，而应该让工程依赖于它。
3. 扩展必须提供一个接口作为出口，该接口可以有多个实现。

## 实践

说了那么多，你或许还是云里雾里，下面我们将开发一个基础`限流扩展`来实操一下。

首先，我们在`src/main/java/com/lin/cms/merak/extensions`扩展目录下新建一个目录`limit`。

并在`src/main/java/com/lin/cms/merak/extensions/limit`目录下新建一个`Limiter.java`接口：

```java
package com.lin.cms.merak.extensions.limit;

import javax.servlet.http.HttpServletRequest;

public interface Limiter {

    boolean handle(HttpServletRequest request);
}
```

`Limiter`接口只有一个需要实现方法，方法拿到请求`request`并根据请求数据来判断请求是否过多。

`limit`扩展需要配置来管理具体的QPS，即一个IP每秒至多请求多少次，配置如下：

```properties
lin.cms.limit.value=5
```

配置文件十分简单，仅一个配置，用来配置QPS值，同时我们也为该配置新建相应的配置类：

```java
package com.lin.cms.merak.extensions.limit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lin.cms.limit")
@PropertySource(value = "classpath:com/lin/cms/merak/extensions/limit/config.properties", encoding = "UTF-8")
public class LimitProperties {

    private Integer value = 5;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
```

有了配置和接口以后，我们需要一个限流具体实现，考虑到基本限流，无需太复杂，我们使用`guava`的RateLimit
来实现。

在`pom.xml`文件中添加上guava的依赖：

```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>28.0-jre</version>
</dependency>
```

再来具体实现一个限流组件`MemoryLimiter`：

```java
package com.lin.cms.merak.extensions.limit;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MemoryLimiter implements Limiter {

    private Map<String, RateLimiter> record = new ConcurrentHashMap<>();

    @Value("${lin.cms.limit.value:5}")
    private Integer value;

    @Override
    public boolean handle(HttpServletRequest request) {
        String uniqueId = getUniqueId(request);
        log.info("uniqueId: {}", uniqueId);
        RateLimiter currentLimiter = record.get(uniqueId);
        if (currentLimiter != null) {
            return currentLimiter.tryAcquire(1);
        } else {
            // 减去当前访问的一次
            RateLimiter limiter = RateLimiter.create(value);
            record.put(uniqueId, limiter);
            return true;
        }
    }

    private String getUniqueId(HttpServletRequest request) {
        return request.getLocalAddr();
    }
}
```

有了限流组件后，我们还需要将组件实施到应用上，我们在`spring mvc`拦截器上
来实现限流，如果某个ip在某一秒内请求太多，则拒绝请求。

因此我们在`limit`目录下新建一个拦截器`LimitInterceptor`：

```java
package com.lin.cms.merak.extensions.limit;

import com.lin.cms.autoconfigure.exception.RequestLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LimitInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Limiter limiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean ok = limiter.handle(request);
        log.info("limit val : {}", ok);
        if (!ok) {
            throw new RequestLimitException();
        }
        return super.preHandle(request, response, handler);
    }
}
```

拦截器的逻辑很简单，如果`limiter`判断当前请求ip请求太多则拒绝，并给出异常，否则进入后面的流程。

我们再将拦截器挂载到web应用中，在`src/main/java/com/lin/cms/merak/common/configure/WebConfig.java`文件中，
我们修改一下代码：

```java
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LimitInterceptor limitInterceptor; // 1
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(limitInterceptor); // 2
        if (authEnabled) {
            //开发环境忽略签名认证
            registry.addInterceptor(authorizeInterceptor)
                    .excludePathPatterns(getDirServePath());
        }
        if (requestLogEnabled) {
            registry.addInterceptor(requestLogInterceptor);
        }
        registry.addInterceptor(logInterceptor);
    }
}
```

添加上面两个地方的代码，我们的限流扩展就可以正常工作了。完成后的代码目录如下：

```bash
src/main/java/com/lin/cms/merak/extensions/limit
├── LimitInterceptor.java
├── LimitProperties.java
├── Limiter.java
├── MemoryLimiter.java
└── config.properties
```

运行程序，如果某个ip的请求过多，则会报`TOO_MANY_REQUESTS`的异常。


<RightMenu />


