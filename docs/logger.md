---
title: 行为日志
---

# 行为日志

本小节建立在你已经阅读和熟悉了上一小节[单元测试](./permission.md)的基础上。

> 行为日志与[日志系统](./logging.md)不是一种东西，日志系统是对于程序运行的日志打点记录，而行为日志是对用户行为的记录。

## 搜索图书

在正式的行为日志介绍前，我们还是得先完善一下我们的图书应用。

在BookController中，我们新增一个`searchBook`方法，并以`search`路径对外暴露API，
考虑到MySQL的模糊搜索，因此我们给前端传入的keyword两边都新增`%`。

```java
public class BookController {
    @GetMapping("/search")
    public List<BookDO> searchBook(@RequestParam(value = "q", required = false, defaultValue = "") String q) {
        List<BookDO> books = bookService.getBookByKeyword("%" + q + "%");
        return books;
    }
}
```

在BookService中，我们也添加上对应的`getBookByKeyword`方法，并在`BookServiceImpl`中实现。

```java
public interface BookService {
    List<BookDO> getBookByKeyword(String s);
}
```

```java
public class BookServiceImpl implements BookService {
    @Override
    public List<BookDO> getBookByKeyword(String q) {
        List<BookDO> books = bookMapper.selectByTitleLikeKeyword(q);
        return books;
    }
}
```

BookServiceImpl随之调用`BookMapper#selectByTitleLikeKeyword`方法来搜索数据
库中的图书。然后BookMapper其实没有`selectByTitleLikeKeyword`方法，我们需要自己
来实现。如下：

```java
package com.lin.cms.merak.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.cms.merak.model.BookDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMapper extends BaseMapper<BookDO> {
    List<BookDO> selectByTitleLikeKeyword(@Param("q") String q);
}

```

这样还不够，因为我们这是定义了代理接口，但并未书写相应的SQL，因此我们还完善`BookMapper.xml`。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.lin.cms.merak.mapper.BookMapper">

    <resultMap id="BaseResultMap" type="com.lin.cms.merak.model.BookDO">
        <id column="id" jdbcType="INTEGER" property="id"/>
        <result column="title" jdbcType="VARCHAR" property="title"/>
        <result column="author" jdbcType="VARCHAR" property="author"/>
        <result column="summary" jdbcType="VARCHAR" property="summary"/>
        <result column="image" jdbcType="VARCHAR" property="image"/>
        <result column="create_time" jdbcType="TIMESTAMP" property="createTime"/>
        <result column="update_time" jdbcType="TIMESTAMP" property="updateTime"/>
        <result column="delete_time" jdbcType="TIMESTAMP" property="deleteTime"/>
    </resultMap>

    <select id="selectByTitleLikeKeyword" resultMap="BaseResultMap">
        SELECT *
        FROM book b
        WHERE
        b.title LIKE #{q}
        AND
        b.delete_time IS NULL
    </select>
</mapper>
```

这样，我们从分别从控制层、业务层、DAO层完善了图书应用，接下来我们测试一下。

```bash
curl http://localhost:5000/v1/book/search\?q\=c
```

结果如下：

```json
[{"id":2,"title":"C程序设计语言","author":"（美）Brian W. Kernighan","summary":"在计算机发展的历史上，没有哪一种程序设计语言像C语言这样应用广泛。本书原著即为C语言的设计者之一Dennis M.Ritchie和著名计算机科学家Brian W.Kernighan合著的一本介绍C语言的权威经典著作。","image":"https://img3.doubanio.com/lpic/s1106934.jpg"}]
```

很好，我们以`c`作为关键字，顺利地从数据库中搜索出了`C程序设计语言`这本书。

我们再来尝试一个关键词`java`：

```bash
curl http://localhost:5000/v1/book/search\?q\=java
```

```json
[]
```

不知道你是否注意到了，在前面的小节中，如果查询图书无果，我们会抛出一个`NotFoundException`异常，
但是在这里我们却未抛出异常，而是不作为，即使结果为`[]`。

这是lin-cms的一种标准，或者说一种妥协，如果返回结果为多个（即列表List或者分页Page），那么即使最后
结果是空的，我们也不抛出异常，而是给出一个空的结果。

## 记录日志

行为日志的使用十分简单，我们再次优化一下BookController代码。

```java
public class BookController {
    @GetMapping("/search")
    @Logger(template = "{user.nickname}搜索的一本书")
    public List<BookDO> searchBook(@RequestParam(value = "q", required = false, defaultValue = "") String q) {
        List<BookDO> books = bookService.getBookByKeyword("%" + q + "%");
        return books;
    }
}
```

我们新增了一行代码`@Logger(template = "{user.nickname}搜索的一本书")`；`Logger`是lin-cms提供的注解，有了
它你就可以方便地使用行为日志。

注意在Logger`template`参数中有`{}`占位符，它会帮助我们做一些很酷的事，你可以猜测一下，我们将在后面揭晓。

我们再次运行程序，并且请求该API。

```bash
curl http://localhost:5000/v1/book/search\?q\=c
```

很不幸，没有任何日志被数据库记录。Logger的使用依赖于权限系统，因为记录日志是针对用户而言的，如果连用户
都没有，那记录的日志也没有意义。

因此我们还得为searchBook添加上相应的RouteMeta和LoginRequired注解。如下：

```java
public class BookController {
    @GetMapping("/search")
    @RouteMeta(permission = "搜索图书", module = "图书", mount = true)
    @LoginRequired
    @Logger(template = "{user.nickname}搜索的一本书")
    public List<BookDO> searchBook(@RequestParam(value = "q", required = false, defaultValue = "") String q) {
        List<BookDO> books = bookService.getBookByKeyword("%" + q + "%");
        return books;
    }
}
```

而后，我们运行并请求，但是我们得先登录获得令牌，并将其加入到请求头中，可参考[上一小节](./permission.md)。

```bash
curl -XPOST -H 'Content-Type:application/json' -d '{"username":"root","password":"123456"}'  localhost:5000/cms/user/login
```

```bash
curl -H 'Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTgwMjg5NDQ4fQ._vWFoX04EPT4ubVVtASbBx7rAeGkVZfO52KhOry6z94' http://localhost:5000/v1/book/search\?q\=c
```

结果就不贴上了，我们又顺利得到了图书数据，并且在数据库`lin-log`表中，我们可以看到新增的行为日志：

```
+----+------------------+---------+----------+-------------+--------+-----------------+--------------+
| id | message          | user_id | username | status_code | method | path            | permission   |
+----+------------------+---------+----------+-------------+--------+-----------------+--------------+
| 2  | 范闲搜索的一本书    | 1       | 范闲     | 200         | GET     | /v1/book/search| 搜索图书      | 
+----+------------------+---------+----------+-------------+--------+-----------------+--------------+
```

我们截取了大部分数据，细致的你一定能够看懂大部分数据。有一处有些奇怪，message字段`范闲搜索的一本书`究竟是怎么来的呢？

在Logger的template中我们明明填入的是`{user.nickname}搜索的一本书`，没错`{}`占位符会被默认解析。

其中的`user`是当前登录的用户，如果用户未登录，那么你便不能使用`{}`占位符，因此无法知道具体的数据，而`user`对应的数据
模型其实是`com.lin.cms.merak.model.UserDO`，它的定义如下：

```java
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名，唯一
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像url
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @JsonIgnore
    @TableLogic
    private Date deleteTime;
}
```

意味着你也可以在`{}`中使用其它的属性，利于`{user.email}`。

## 已登录用户

在上面我们谈到，用户通过令牌携带信息，lin-cms解析令牌后便可知道当前登录的用户信息。
在lin-cms中，我们可以很便捷的拿到登录用户，如下（伪代码）：

```
import com.lin.cms.merak.common.LocalUser;

public UserPermissionsVO getPermissions() {
        UserDO user = LocalUser.getLocalUser();
        // ***
    }
```

注意：如果你需要通过`LocalUser`来获得当前登录用户，那么请一定要保证有登录的用户，所以
应该如下两个条件：

1. 请求必须携带有效的令牌，令牌损坏或令牌过期均不行。
2. 你的接口方法，如`searchBook`必须被`LoginRequired`修饰，或者`GroupRequired`、`AdminRequired`修饰。

## 总结

在本小节中，我们带你使用了lin-cms的行为日志，详细的说明了登录用户的作用与有效范围。

最后，我们附上一些参考资料供你阅读。

好用的orm框架[mybatis](https://mybatis.org/mybatis-3/zh/index.html)。

好用的mybatis增强框架[mybatis-plus](https://mybatis.plus/)。

<RightMenu />