---
title: 权限管理
---

# 权限管理

本小节建立在你已经阅读和熟悉了上一小节[单元测试](./unit.md)的基础上。

本小节，我们一起来学习lin-cms的核心功能点——权限。lin-cms提供了一套开箱即用的
权限系统，并且有与之对应的前端实现。

lin-cms的权限使用也是非常简单的，它的权限控制基本单位是API接口，即一个API接口
可能会对应一个权限，但是一个权限可能会对应多个API，一般情况下我们都推荐一个API
对应一个权限。

## deleteBook 接口

首先，我们仍然完善一下图书应用，新建一个`deleteBook`API来提供图书的删除，考虑到
程序员的懒惰，我们贴上全部的代码，如下：

```java
package com.lin.cms.merak.controller.v1;

import com.lin.cms.autoconfigure.exception.NotFoundException;
import com.lin.cms.merak.common.utils.ResponseUtil;
import com.lin.cms.merak.dto.book.CreateOrUpdateBookDTO;
import com.lin.cms.merak.model.BookDO;
import com.lin.cms.merak.service.BookService;

import com.lin.cms.merak.vo.UnifyResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/v1/book")
@Validated
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/{id}")
    public BookDO getBook(@PathVariable(value = "id") @Positive(message = "id必须为正整数") Long id) {
        BookDO book = bookService.getById(id);
        if (book == null) {
            throw new NotFoundException("book not found", 10022);
        }
        return book;
    }

    @PostMapping("")
    public UnifyResponseVO createBook(@RequestBody @Validated CreateOrUpdateBookDTO validator) {
        bookService.createBook(validator);
        return ResponseUtil.generateUnifyResponse(10);
    }

    @DeleteMapping("/{id}")
    public UnifyResponseVO deleteBook(@PathVariable("id") @Positive(message = "{id}") Long id) {
        BookDO book = bookService.getById(id);
        if (book == null) {
            throw new NotFoundException("book not found", 10022);
        }
        bookService.deleteById(book.getId());
        return ResponseUtil.generateUnifyResponse(12);
    }
}
```

在`deleteBook`接口中，我们先监测了一下book是否存在，如果不存在则抛出`NotFoundException`异常，
否则我们调用`deleteById`来通过业务接口来删除图书。

`deleteBook`返回的结果，也是我们前面小节中谈到的`UnifyResponseVO`，不过它的消息已经发生了变化，
因此我们重新定义了`12`的消息码。

```properties
lin.cms.code-message[12]=删除图书成功
```

```java
package com.lin.cms.merak.service;

import com.lin.cms.merak.dto.book.CreateOrUpdateBookDTO;
import com.lin.cms.merak.model.BookDO;

public interface BookService {

    BookDO getById(Long id);

    boolean createBook(CreateOrUpdateBookDTO validator);

    boolean deleteById(Long id);
}
```

```java
package com.lin.cms.merak.service.impl;

import com.lin.cms.merak.dto.book.CreateOrUpdateBookDTO;
import com.lin.cms.merak.mapper.BookMapper;
import com.lin.cms.merak.model.BookDO;
import com.lin.cms.merak.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public BookDO getById(Long id) {
        BookDO book = bookMapper.selectById(id);
        return book;
    }

    @Override
    public boolean createBook(CreateOrUpdateBookDTO validator) {
        BookDO book = new BookDO();
        book.setAuthor(validator.getAuthor());
        book.setTitle(validator.getTitle());
        book.setImage(validator.getImage());
        book.setSummary(validator.getSummary());
        return bookMapper.insert(book) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return bookMapper.deleteById(id) > 0;
    }
}
```

在`BookServiceImpl`中，我们实现了`deleteById`方法，它会调用mapper的`deleteById`
方法，该方法已经默认定义在了mapper中，且会自动根据`TableLogic`注解来软删除图书。


## 权限使用

我们知道删除图书是一个比较危险的操作，不能让所有人都可以访问它，它应该只对部分人开放，这也是
权限系统的初衷。

想要使用权限，我们需要先将删除图书API添加至权限控制中，如下（省略了诸多代码）：

```
import com.lin.cms.core.annotation.GroupRequired;
import com.lin.cms.core.annotation.RouteMeta;

@DeleteMapping("/{id}")
@RouteMeta(permission = "删除图书", module = "图书", mount = true)
@GroupRequired
public UnifyResponseVO deleteBook(@PathVariable("id") @Positive(message = "{id}") Long id) {
    BookDO book = bookService.getById(id);
    if (book == null) {
        throw new NotFoundException("book not found", 10022);
    }
    bookService.deleteById(book.getId());
    return ResponseUtil.generateUnifyResponse(12);
}
```

我们为`deleteBook`方法新增加了两个注解，一个是`RouteMeta`，它会将其修饰的方法，即`deleteBook`纳入到
权限系统中，并接受保护。RouteMeta共有三个参数，它们的作用如下：

- permission 权限名称，如：删除图书，用来给权限命名
- module 权限模块，如：图书，表示该权限属于图书模块
- mount 是否挂载到权限系统中，如果为 false ，则该权限不挂载

纳入到了权限系统中，并且收到了保护，可是这还不够，我们需要再告诉lin-cms究竟谁能够访问它，因此第二个注解
`GroupRequired`上场了，它表示`deleteBook`这个接口，必须由 **拥有删除图书这个权限的分组** 才能访问。

可能，你还对GroupRequired不甚理解，没关系，下面我们再会详细介绍到它。

我们运行代码，看权限系统是不是起作用了，由于我们在测试环境，权限拦截默认是关闭的，因此我们还需要开启它。
打开`src/main/resources/application-dev.properties`，在里面修改`auth.enabled`的值为true：

```properties
# 开启权限拦截
auth.enabled=true
```

随后运行：

```bash
mvn spring-boot:run 
```

```bash
curl -XDELETE localhost:5000/v1/book/1
```

结果：

```json
{"code":10012,"message":"请传入认证头字段","request":"DELETE /v1/book/1"}
```

我们发现，我们被拒绝了，且提示我们传入`认证头字段`，即`Authorization`字段，它是权限认证的一种字段。
lin-cms默认使用`jwt`作为校验媒介，如果你不够了解，可以阅读[jwt](./token.md)一节。

因此，我们需要登录拿到令牌，且将令牌放到HTTP请求头部，因此我们使用登录接口尝试登录：

```bash
curl -XPOST -H 'Content-Type:application/json' -d '{"username":"root","password":"123456"}'  localhost:5000/cms/user/login
```

结果如下：

```json
{
"access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTgwMjA3MzU3fQ.vUoputrD413--DM1bbWaE4vZjkBu6r24zaQeBfMf338",
"refresh_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoicmVmcmVzaCIsImV4cCI6MTU4Mjc5NTc1N30.gfSEk4vNg9rBDkwgIy49-Yqw9PJ6rd1-nzyi2zi92-A"
}
```

对于双令牌的机制，如果你不了解，可以阅读[jwt](./token.md)一节。

得到令牌后，我们加入到请求头部中，并再次请求删除图书接口。

```bash
curl -H 'Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTgwMjA3MzU3fQ.vUoputrD413--DM1bbWaE4vZjkBu6r24zaQeBfMf338' -XDELETE localhost:5000/v1/book/1
```

结果如下：

```json
{"code":12,"message":"删除图书成功","request":"DELETE /v1/book/1"}
```

## 权限系统

我们申请了令牌然后在请求头加入了令牌字段后，令牌生效，我们便成功地删除了图书。

总结一下，我们的权限是如何使用的？

- 定义API接口，API是权限的基本单位，但并不是每个API接口都需要权限，如删除图书这样慎重的操作需要权限。
- 接口需要RouteMeta注解来将其纳入权限系统，并且需要开启`auth.enabled`配置才能在开启权限拦截。
- 接口需要类似GroupRequired的注解来告诉lin-cms，该接口对哪些人开启。

接下来，我们从整体上把控一下我们的权限系统。

权限的桥梁是令牌，lin-cms会从用户携带的令牌中判断用户的身份。

权限是分级的，如GroupRequired表示分组权限，即只有加入分组后的用户，且该分组已经拥有该权限，用户才能访问接口。
类似的还有，`LoginRequired`表示用户登录后即可访问，非登录用户不可访问；还有`AdminRequired`表示管理员才可访问。

|     等级        |         作用        |                     说明                                ｜
| :------------: | :----------------: | :------------------------------------------------------: |
|  LoginRequired |   拦截未登录用户      | 默认新建用户为guest分组，游客分组，该分组也可登录，但不可分配权限 ｜
|  GroupRequired | 拦截非已有权限分组用户 | 用户加入分组后，再为分组分配权限，该用户才能访问权限接口         ｜
|  AdminRequired | 拦截非管理员用户      | root分组用户即时管理员用户，且目前root只能有一个用户           ｜

lin-cms默认只有一个管理员分组，即root分组，且root分组只有一个用户——root，用户新建时若无特殊特定默认在guest分组中。

当然关root分组、root用户和guest分组，你如果需要调整它，可以在`src/main/resources/application.properties`配置
文件中修改它们：

```properties
# 默认的权限分组id和名称配置
# 默认root分组名称为 root
group.root.name=root
# 默认root分组id为 1
group.root.id=1
# 默认游客分组名称为 guest
group.guest.name=guest
# 默认游客分组id 为 2
group.guest.id=2
# 默认 root 用户id为 1
user.root.id=1
# 默认 root 用户 用户名 为 root
user.root.username=root
# 默认 root 用户 昵称 为 root
user.root.nickname=root
```

你也必须修改你数据库相应的数据，你可以在`src/main/resources/schema.sql`文件中，更改需要的插入数据：

```sql
-- ----------------------------
-- 插入超级管理员
-- 插入root分组
-- ----------------------------
BEGIN;

INSERT INTO lin_user(id, username, nickname)
VALUES (1, 'root', 'root');

INSERT INTO lin_user_identity (id, user_id, identity_type, identifier, credential)
VALUES (1, 1, 'USERNAME_PASSWORD', 'root',
        'pbkdf2sha256:64000:18:24:n:yUnDokcNRbwILZllmUOItIyo9MnI00QW:6ZcPf+sfzyoygOU8h/GSoirF');

INSERT INTO lin_group(id, name, info)
VALUES (1, 'root', '超级用户组');

INSERT INTO lin_group(id, name, info)
VALUES (2, 'guest', '游客组');

INSERT INTO lin_user_group(id, user_id, group_id)
VALUES (1, 1, 1);

COMMIT;
```

这是一些比较危险的操作，我们不推荐你更改，如果你觉得lin-cms权限系统无法满足你的业务需求，可以尝试去更改它。

## 总结

在本小节中，我们带你使用了lin-cms的权限，并介绍了lin-cms整体的权限系统，当然我们仍然建议你
好好把握自己业务的需求，对于权限系统，它十分重要，是核心，它的更改会让前端实现也需要变化，因此
请仔细斟酌权限系统的更改。

最后，我们附上一些参考资料供你阅读。

jwt权限文档及使用[jwt.io](https://jwt.io/)。

<RightMenu />