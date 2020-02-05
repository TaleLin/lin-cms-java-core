---
title: 项目规范
---

# 项目规范

本小节，我们将介绍 `lin-cms` 的目录结构和代码规范，希望读者能够理解并且运用到自己的开发中。

## 目录结构

以前每谈及java，笔者的第一反应就是`麻烦`，这个麻烦集中在两个点上：一、臃肿啰嗦的代码；二、繁陈复杂的目录结构。

后来笔者分别使用`python`，`php`以及`node.js`等语言完成过实际项目，在开发，测试，维护，运维等诸多环节中兜兜转转，
笔者忽然觉得`java`的`麻烦`实则是规范的精髓。

看似啰嗦的代码却恰巧代表了代码的严谨，复杂的目录结构也正好表明项目的工整。多层的组织降低了代码的耦合性，提高了
复用性，给重构和维护带来了极大的方便。

`lin-cms-java`的目录结构参考了大量的spring-boot项目，严格遵守MVC的代码组织，
不仅如此，为了让项目更加严谨，我们研读了阿里巴巴规约手册中的java内容，吸收了大量的既成规范。

项目目录结构如下：

```
├── bo bussiness object 业务类
├── common 公共类库
│   ├── aop aop相关
│   ├── configure 配置相关
│   ├── consts 常量类
│   ├── exception 异常处理
│   ├── interceptor 拦截器
│   ├── mybatis mybatis相关
│   └── utils 工具类
├── controller 控制层
│   ├── cms cms视图
│   └── v1 api视图
├── dto data access object
│   ├── admin
│   ├── book
│   └── user
├── extensions 扩展
│   ├── file 文件扩展
├── mapper mybatis mapper
├── model 数据模型
├── service 业务层
└── vo view object 视图类
```

在每个目录的后面，我们都以注释的方式标明了其作用，当然还需要着重说明如下几点。

1. bo，vo，do，dto究竟是什么？

`o`是object的缩写，即对象的意思，主要用来封装某一层返回的数据结果，
`b`表示`bussiness`，连起来就是业务对象类，用来封装业务层返回的结果；
同理 `vo`全称`view object`，用来封装视图层的返回结果；`dto`全称`data
transform object`，用来封装传输层的数据，如校验类。

2. mapper和service

`mapper`的概念主要源于`mybatis`，存放mapper相关的接口；service是业务接口及
实现，每一个`service`最好都有其接口定义与具体实现，方便接口重写。

3. controller和model

`controller`即视图层，`model`是模型层，里面的数据类与数据表一一对应，也就是
阿里规范中的`do`。

4. common

公共类库，里面主要是与工程相关的基础类库，如`自动配置类`等。

关于`extension`将在后面的小节中介绍。

以上的目录可能并非你自己的项目的最终目录，它只是`lin-cms`在诸多实践中的讨论与沉淀，
希望它能为你在项目规范与开发上帮上一把。

## 规范

### 异常规范

lin-cms提供了基础的异常类，这些异常类继承自`HttpException`，当程序抛出这些异常时，
默认的异常处理机制会将异常信息以规定的格式返回给前端。

lin-cms的异常类中主要有三个属性——`message`，`httpCode`，`code`。

`message`是异常携带的信息，也是java异常中默认的信息字段，你可以通过构造函数的方式去
改变它：

```
throw new HttpException("message");
```

`httpCode`是返回给前端的http status，表示当前请求的状态，我们推荐每一种 http status 都
应该有一个相应的异常类，如`ForbiddenException`。

`code`表示消息码，如 `9999`消息码，每个消息码都有默认对应的英文消息，以及配置的中文消息；
我们通过消息码这个媒介来处理中、英文消息的转换，从而实现国际化。

当前端请求触发了一个异常，异常被程序捕捉后，以如下相应结构返回：

```json
{
    "code": 10051,
    "message": "令牌过期",
    "request": "POST /cms/admin/permission/remove"
}
```

相应中有刚才提到的`code`和`message`，以及前端请求的部分信息`request`。

可以看到，`10051`消息码携带了其对应的`message`，且 `message` 是中的，默认情况下
`message`是英文的，如果你在相应的配置文件中进行了配置，消息则返回中文。

配置文件为`resources/code.properties`。

### 操作成功规范

如果你操作成功了，lin-cms有两种情况下返回结果。

1. 获取信息，比如获取用户信息，返回结果直接就是`json`格式下的用户信息。
2. 操作信息，如过删除某个用户成功了，用户信息由于被删除了，不可能返回该用户信息了，
   但是仍需要告诉前端操作结果。这个结果的类似于：
```json
{
    "code": 0,
    "message": "OK",
    "request": "POST /cms/admin/permission/remove"
}
```  

这于异常信息的结构一致，因此为了区分异常和成功，我们规定`code`小于10000的作为
成功的消息码，否则应该是失败消息码。

### 接口规范

lin-cms遵守restful规范，即增（POST）删（DELETE）查（GET）改（PUT），请尽量按照该
规范来定义你自己的接口。

### 业务规范

我们推荐你为每一个`Service`都定义一个接口以及接口实现，如`AdminService`和`AdminServiceImpl`。

### 校验规范

如果一个接口需要做参数校验，我们推荐使用spring-boot-validation-starter，且为每一个需要
校验的接口定一个校验类，类似于：

```java
@Data
public class NewGroupDTO {
    @NotBlank(message = "{group.name.not-blank}")
    @Length(min = 1, max = 60, message = "{group.name.length}")
    private String name;

    @Length(min = 1, max = 255, message = "{group.info.length}")
    private String info;

    @LongList(allowBlank = true, message = "{permission.ids.long-list}")
    private List<Long> permissionIds;
}
```

考虑到校验信息的统一处理，我们通过占位符的方式来定义校验失败的信息，如`{group.name.not-blank}`。

你可以在resources目录下的`ValidationMessages.properties`配置该信息。

::: tip

规范是一件繁琐却又必要的事情，如果你有更好的规范推荐，欢迎一起完善！

:::

<RightMenu />