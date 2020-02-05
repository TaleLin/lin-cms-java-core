---
title: 快速上手
---

# 快速上手

本小节我们将在`lin-cms`的基础上开发一个简单的图书demo，帮助大家来熟悉和入门`lin-cms`。

`lin-cms`的是一个lin团队经数次打磨的模板项目，是我们团队在`spring-boot`的基础上沉淀下来的
最佳实践，我们为你准备了丰富和实用的工具和库，帮助你在最少的时间里获得最大的便利。

请确保你已经从`github`或其它途径上获取了`lin-cms-java`的模板项目。

> 注意：本小节建立在你有一定spring-boot、spring-mvc和mybatis的基础上。

## 数据层

**数据是应用的基石**。 首先，我们需要为`图书`设计一张数据表，如下：

```mysql
DROP TABLE IF EXISTS book;
CREATE TABLE book
(
    id          int(11)     NOT NULL AUTO_INCREMENT,
    title       varchar(50) NOT NULL,
    author      varchar(30)          DEFAULT NULL,
    summary     varchar(1000)        DEFAULT NULL,
    image       varchar(100)         DEFAULT NULL,
    create_time datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    delete_time datetime(3)          DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
  
INSERT INTO book(`title`, `author`, `summary`, `image`) VALUES ('深入理解计算机系统', 'Randal E.Bryant', '从程序员的视角，看计算机系统！\n本书适用于那些想要写出更快、更可靠程序的程序员。通过掌握程序是如何映射到系统上，以及程序是如何执行的，读者能够更好的理解程序的行为为什么是这样的，以及效率低下是如何造成的。', 'https://img3.doubanio.com/lpic/s1470003.jpg');
INSERT INTO book(`title`, `author`, `summary`, `image`) VALUES ('C程序设计语言', '（美）Brian W. Kernighan', '在计算机发展的历史上，没有哪一种程序设计语言像C语言这样应用广泛。本书原著即为C语言的设计者之一Dennis M.Ritchie和著名计算机科学家Brian W.Kernighan合著的一本介绍C语言的权威经典著作。', 'https://img3.doubanio.com/lpic/s1106934.jpg');
```

`book`除了`id`、`title（标题）`和`summary（概述）`等基本信息外，还有`create_time`和`update_time`等额外数据，
它们的类型是`datetime`，至于它们的用处，我们将在下面一一展开。

`lin-cms`内置了`mybatis-plus`的代码生成器，因此我们完全可以通过数据表迅速生成相关的业务模板代码。

请现在你的数据库中执行上面的`SQL`脚本，后面的所有操作均依赖与它。

## 模型层

数据库有了`book`数据表以后，我们需要在代码中添加上与之对应的模型，一般而言如果某个类用来表示数据模型，
那么该类就是比较单纯的数据容器（Data Object），我们简称`DO`，因此我们在`src/main/java/com/lin/cms/merak/model`
路径下，新建一个名为`BookDO.java`的模型类。

```java
package com.lin.cms.merak.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@TableName("book")
@Data
public class BookDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private String author;

    private String summary;

    private String image;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @JsonIgnore
    @TableLogic
    private Date deleteTime;
}
```

`BookDO`是与`book`表对应的模型类，它的每个属性都与数据表字段对应，我们还需要为`BookDO`添加
上`TableName`注解，注解中的`book`值是数据库中表的名称。

更加重要的是，我们需要给主键id打上`TableId`注解，`value`值就是数据表中主键对应的名称，`type`则
表示该主键类型。

接下来，我们来详细说明一下三个日期类型的字段：

- createTime 用来表示book被创建的时间，方便以后可能会有的数据分析
- updateTime 用来表示book被更新时所记录的时间
- deleteTime 用来表示book被删除的时间

我们分别为这三个字段都打上了`JsonIgnore`注解，是为了在json序列化的时候忽略它们。

`TableLogic`是mybatis-plus提供的逻辑删除（软删除）注解，有了该注解后，当你调用API删除
某个book的时候，并不会真正的删除book，而是将`deleteTime`设置为删除时间。

## 业务层

按照mybatis的惯例，有了数据模型以后，我们还需要为它提供相应的`mapper`。

我们分别在`src/main/java/com/lin/cms/merak/mapper`和`src/main/resources/mapper`
下新建`BookMapper.java`文件和`BookMapper.xml`文件。

BookMapper.java:

```java
package com.lin.cms.merak.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.cms.merak.model.BookDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMapper extends BaseMapper<BookDO> {
}
```

这里我们继承mybatis-plus的`BaseMapper`，BaseMapper可以让我们的BookMapper默认就拥有
很大使用的API，并给BookMapper打上了`Repository`注解，方便spring-boot识别。

BookMapper.xml:

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
</mapper>
```

在BookMapper.xml文件中，我们定义了基础了映射，即模型（BookDO）与数据表的映射，方便后续使用。

有了基础Mapper以后，我们再为book定义相关的业务接口和业务实现。

我们分别在`src/main/java/com/lin/cms/merak/service`和`src/main/java/com/lin/cms/merak/service/impl`
下新建`BookService.java`文件和`BookServiceImpl.java`文件，其中BookServiceImpl是BookService的实现。

```java
package com.lin.cms.merak.service;

public interface BookService {
}
```

```java
package com.lin.cms.merak.service.impl;

import com.lin.cms.merak.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    
}
```

在`BookServiceImpl`上，我们打上了`Service`注解表示它是一个业务层类。

## 控制层

接下来，我们需要给book创建对应的控制器，并以此对外提供访问接口。

在`src/main/java/com/lin/cms/merak/controller/v1`下我们新建`BookController.java`文件：

```java
package com.lin.cms.merak.controller.v1;

import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/v1/book")
public class BookController {
    
}
```

`RestController`注解表示BookController是控制器类，其遵循Rest风格，`RequestMapping`指定BookController的`url`前缀。

## getById 接口实现

有了上面的基础后，我们再来完成API接口，首先我们需要为BookController添加一个`getBook`接口，让前端通过`id`可以
获取后端的`book`数据，即：

```java
package com.lin.cms.merak.controller.v1;

import com.lin.cms.merak.model.BookDO;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/v1/book")
@Validated
public class BookController {
    
   @GetMapping("/{id}")
   public BookDO getBook(@PathVariable(value = "id") @Positive(message = "id必须为正整数") Long id) {
       return null;
   }
}
```

我们为`getBook`接口添加了基础代码，`GetMapping`注解指定该接口需要GET方法请求，且请求路径为`/{id}`，
注意`id`为变量，是路径变量，因此可通过`PathVariable`修饰的`Long id`值来得到该变量值。

同时我们也为`id`添加上了一个校验注解，即`Positive`，规定`id`必须是正整数，并在BookController
上添加上了`Validated`注解，这样该校验才会生效。

`getBook`返回`BookDO`，目前我们未实现具体业务，让其返回`null`。

我们需要通过前端传入的`id`来调用服务拿到图书数据，因此接下来我们将完善业务层。

首先，为BookService添加一个获取图书的方法：

```java
package com.lin.cms.merak.service;

import com.lin.cms.merak.model.BookDO;

public interface BookService {
    
    BookDO getById(Long id);
}
```

然后我们需要在BookServiceImpl中去实现这个方法：

```java
package com.lin.cms.merak.service.impl;

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
}
```

`getById`的实现很简单，通过`BookMapper`实例来传入图书id，调用`selectById`方法
即可查询数据库得到book数据。

注意：BookMapper因为继承了BaseMapper，所以它默认就具有大量的方法，其中就包括selectById。

这里的`bookMapper`是通过spring的依赖注入来实现的，所以会有`Autowired`注解来修饰它。

完成了业务层以后，我们再返回控制层完善代码：

```java
package com.lin.cms.merak.controller.v1;

import com.lin.cms.merak.model.BookDO;
import com.lin.cms.merak.service.BookService;

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
        return book;
    }
}
```

我们修改了刚才的BookController，通过`Autowired`注解我们也拿到了`bookService`实例，并在getBook
函数中调用了它的`getById`方法，并返回book。


我们可以测试一下这个接口：

从终端运行项目：

```bash
mvn spring-boot:run 
```

如果你使用idea这样的IDE，可以直接在界面按钮上运行项目。

使用curl测试接口：

```bash
curl localhost:5000/v1/book/1 
```

结果如下：

```json
{
"id":1,
"title":"深入理解计算机系统",
"author":"Randal E.Bryant",
"summary":"从程序员的视角，看计算机系统！\n本书适用于那些想要写出更快、更可靠程序的程序员。通过掌握程序是如何映射到系统上，以及程序是如何执行的，读者能够更好的理解程序的行为为什么是这样的，以及效率低下是如何造成的。","image":"https://img3.doubanio.com/lpic/s1470003.jpg"
}
```

## 完善异常

我们的应用目前已经可以顺利的获得图书数据了，试想一下如果如果前端的参数不规范，传入`1ll`这样
的id参数，那该怎么办了？

不用担心，我们已经通过`@Positive(message = "id必须为正整数") Long id`这样的代码规定id参数
必须为正整数，若参数不符合规范，程序会给出相应的错误提示返回给前端。

可是如果前端传入的id数据库压根没有怎么办，即`bookService.getById(id)`查询的结果为`null`，如果直接
返回`null`给前端，那么肯定不友好，我们可以给前端一个异常提示，如下：

```java
package com.lin.cms.merak.controller.v1;

import com.lin.cms.autoconfigure.exception.NotFoundException;
import com.lin.cms.merak.model.BookDO;
import com.lin.cms.merak.service.BookService;

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
            throw new NotFoundException("未找到图书");
        }
        return book;
    }
}
```

若`book`为`null`，我们则抛出`NotFoundException`异常，异常消息为`book not found`。

再次通过curl测试一下：

```bash
mvn spring-boot:run 
curl localhost:5000/v1/book/100
```

结果如下：

```json
{"code":10020,"message":"未找到图书","request":"GET /v1/book/100"}
```

因为我们数据库中没有id为`100`的图书，因此我们抛出了一个NotFoundException的异常，lin-cms
有专门的异常处理机制来处理该异常，然后返回给前端一个清晰的异常信息。

在这个异常信息中有三个重要字段，我们来分别说明一下：

- code字段：消息码，用来唯一标识一条消息，你可以把它理解为`未找到图书`这条消息的id
- message字段：消息体，用来表示异常消息，如 `未找到图书`
- request字段：请求信息，告诉请求者，是哪一个请求发生了异常，方便前端排查

如果你的应用对`国际化`没有要求，那么此时你的异常处理其实已经足够了，注意：大部分人其实已经
足够了。但是，你真的需要国际化或者想要更加人性化的异常信息处理，那么这样的异常信息是不够好的。

我们把`未找到图书`这样的异常消息硬编码进了异常类中，如果在后面的开发中需要更改它，那么你可能会
花上一番功夫来找它了，因此我们提供了配置文件的机制来更改异常消息。

或许你已经猜到了，`code`既然用来唯一标识一条异常信息，那么肯定有它的作用。

我们为`未找到图书`这条异常信息重新定义一个code码，记住每一条信息都对应一个code，如果你的异常信息
是新的，那么肯定需要重新定义一个code码，且不能覆盖原来已经存在的code码。

code码的定义在`src/main/resources/code.properties`配置文件中，如我们为`未找到图书`定义消息码
为`10022`：

```properties
lin.cms.code-message[10022]=未找到相关书籍
```

code-message是一个哈希表配置，不同的code码对应不同的异常信息，当然code码的定义是需要符合规范的，
lin-cms规定若code码大于`10000`，如`10022`表示对应的消息是异常消息，如果小于`10000`表示该消息
是正常的消息，如`0`表示成功。

配置完毕后，我们再改善一下我们的BookController代码，如下：

```java
package com.lin.cms.merak.controller.v1;

import com.lin.cms.autoconfigure.exception.NotFoundException;
import com.lin.cms.merak.model.BookDO;
import com.lin.cms.merak.service.BookService;

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
}
```

我们替换了`NotFoundException`中的默认异常信息，将其改成了英文，因为默认的异常信息绝大多数都是
英文，并且传入了我们刚才所定义的`code`码——10022。

再次运行：

```bash
mvn spring-boot:run 
curl localhost:5000/v1/book/100
```

结果：

```json
{"code":10022,"message":"未找到相关书籍","request":"GET /v1/book/100"}
```

可以看到，及时NotFoundException中的异常信息是英文，但返回给前端的异常信息却是中文，
且与我们刚在在`code.properties`中的配置一致。

当然如果你偷懒或者觉得这样做没有实际的收益，完全可以直接硬编码成中文。

## 请求体校验

接下来，我们为了图书新增一个接口——创建图书。

```java
package com.lin.cms.merak.controller.v1;

import com.lin.cms.autoconfigure.exception.NotFoundException;
import com.lin.cms.merak.common.utils.ResponseUtil;
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
    public UnifyResponseVO createBook() {
        return ResponseUtil.generateCreatedResponse("创建图书成功");
    }
}
```

我们在BookController中新增了一个createBook方法，且通过`PostMapping`注解暴露这个接口，请求方法为`POST`，
请求路径为`/v1/book`。

createBook方法返回一个`UnifyResponseVO`的对象，这个对象是lin-cms提供的统一相应对象，该对象其实与刚才的
异常信息体一致，即：

```json
{"code":10022,"message":"未找到相关书籍","request":"GET /v1/book/100"}
```

lin-cms还提供了`ResponseUtil`帮助类来帮助你迅速地创建该对象，即`generateCreatedResponse`方法，你
只需传入`message`参数——`创建图书成功`，则前端自然会得到更加全面的信息。

我们可以测试一下这个接口：

```bash
mvn spring-boot:run 
```

```bash
curl -XPOST localhost:5000/v1/book
```

结果如下：

```json
{"code":1,"message":"创建图书成功","request":"POST /v1/book"}
```

你可以发现，此处的相应内容与上面的异常结果几乎一致，当然也遵循我们的规范，如果返回的是正常信息，那么
code码必须小于`10000`。

我们还需完善我们的接口，首先接口需要从请求体中读取新建图书的数据，其次对于图书的数据我们需要做检验，不能
让不合法数据进来。

我们在`src/main/java/com/lin/cms/dto`下新建包`book`，并在新建的book包下新建`CreateOrUpdateBookDTO.java`文件：

如下：

```java
package com.lin.cms.merak.dto.book;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateBookDTO {

    @NotEmpty(message = "必须传入图书名")
    @Size(max = 50, message = "图书名不能超过50字符")
    private String title;

    @NotEmpty(message = "必须传入图书作者")
    @Size(max = 50, message = "图书作者名不能超过30字符")
    private String author;

    @NotEmpty(message = "必须传入图书综述")
    @Size(max = 1000, message = "图书综述不能超过1000字符")
    private String summary;

    @Size(max = 100, message = "图书插图的url长度必须在0~100之间")
    private String image;
}
```

我们把有关于数据传输的数据容器类称之为`DTO（Data Transfrom Object）`，CreateOrUpdateBookDTO共有4个字段，
分别对于book其中的4个字段，且每个字段都有对于校验注解，如`NotEmpty`，虽然这些注解都有默认校验异常信息，
不过我们推荐你为每一个校验注解都定义上与之相符的`message`，给前端更加友好的提示。

有了该类后，我们再来完善BookController和BookService，如下：

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
        return ResponseUtil.generateCreatedResponse("创建图书成功");
    }
}
```

为CreateOrUpdateBookDTO打上了`RequestBody`（从请求体中读数据）和`Validated`（校验请求体）注解。


```java
package com.lin.cms.merak.service;

import com.lin.cms.merak.dto.book.CreateOrUpdateBookDTO;
import com.lin.cms.merak.model.BookDO;

public interface BookService {

    BookDO getById(Long id);

    boolean createBook(CreateOrUpdateBookDTO validator);
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
}
```

实现了`createBook`方法，通过`bookMapper`向数据库中插入数据。

我们再次测试：

```bash
curl -XPOST -H 'Content-Type:application/json' -d '{"title":"大江东去","author":"pedro","summary":"summary"}'  localhost:5000/v1/book
```

结果：

```json
{"code":1,"message":"创建图书成功","request":"POST /v1/book"}
```

## 总结

在本小节中，我们为图书应用添加了两个基础的API接口，并探讨了异常信息的规范、配置化和国际化。

本节涉及内容比较多，需要你花费一定的时间消化，当然如果你十分熟悉spring-boot和spring-mvc，
完全可以走马观花的阅读本节。

最后，我们附上一些参考资料供你阅读。

好用的mybatis增强框架[mybatis-plus](https://mybatis.plus/)。
spring-boot开发rest应用[Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)。
spring-boot校验请求体[Validating Form Input](https://spring.io/guides/gs/validating-form-input/)。

<RightMenu />