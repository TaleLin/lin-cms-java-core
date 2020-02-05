---
title: 单元测试
---

# 单元测试

本小节建立在你已经阅读和熟悉了上一小节[快速上手](./starter.md)的基础上。

上一节中，我们完成了图书的两个基本API，并从中逐步提到了一些lin-cms规范，
本小节我们将继续以图书为支撑点进行应用的开发教程。

## 校验配置

在上一节中，我们在新建图书接口的开发中，使用到了一个校验类——CreateOrUpdateBookDTO：

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

不知你是否记得，我们在异常消息处理的时候谈到过，异常消息如果硬编码，那么如果后续
修改的话，会将耗费一定的时间去寻找它，且不易维护。

这句话放在校验类中同样适用，类似于`必须传入图书名`这样的硬编码异常信息，你确实可以
把它以硬编码的形式来处理，但是如果提供配置文件的方式确实更好，而且spring-boot已经
帮我们做了这项工作，我们在`src/main/resources/ValidationMessages.properties`
中添加上关于CreateOrUpdateBookDTO的校验信息配置：

```properties
# book
book.title.not-empty=必须传入图书名
book.title.size=图书名不能超过50字符
book.author.not-empty=必须传入图书作者
book.author.size=图书作者名不能超过30字符
book.summary.not-empty=必须传入图书综述
book.summary.size=图书综述不能超过1000字符
book.image.size=图书插图的url长度必须在0~100之间
```

有了该配置后，我们修缮一下我们CreateOrUpdateBookDTO代码。

```java
package com.lin.cms.merak.dto.book;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class CreateOrUpdateBookDTO {

    @NotEmpty(message = "{book.title.not-empty}")
    @Size(max = 50, message = "{book.title.size}")
    private String title;

    @NotEmpty(message = "{book.author.not-empty}")
    @Size(max = 50, message = "{book.author.size}")
    private String author;

    @NotEmpty(message = "{book.summary.not-empty}")
    @Size(max = 1000, message = "{book.summary.size}")
    private String summary;

    @Size(max = 100, message = "{book.image.size}")
    private String image;
}
```

在代码中，我们替换了硬编码，而是通过`{}`占位符来写入配置名，spring-boot
会自动帮我们填充上配置信息。

如果你是极致主义者，你会发现，其实我们还有一处仍然存在消息的硬编码，在BookController中：

```
@PostMapping("")
public UnifyResponseVO createBook(@RequestBody @Validated CreateOrUpdateBookDTO validator) {
    bookService.createBook(validator);
    return ResponseUtil.generateCreatedResponse("创建图书成功");
}
```

和异常信息处理一样，我们在`src/main/resources/code.properties`配置文件中，添加上关于`创建图书成功`
的消息码配置，记住这样正常的消息码必须小于10000。

```properties
lin.cms.code-message[10]=新建图书成功
```

且修改一下BookController：

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
}
```

随后，我们来测试一下代码，但这次我们故意让校验失败，来看一下配置是否生效。

```bash
mvn spring-boot:run 
```

```bash
curl -XPOST -H 'Content-Type:application/json' -d '{"author":"pedro","summary":"summary"}'  localhost:5000/v1/book
```

结果如下：

```json
{"code":10030,"message":{"title":"必须传入图书名"},"request":"POST /v1/book"}
```

从结果中，可以发现配置生效了，但是`message`字段却发生了变化，由字符串变成了一个对象。没错因为参数校验
会存在多个字段校验，且每个字段都有可能会出错，我们不可能一次只给出一个字段的错误，因此当发生校验失败是，
它的异常信息往往是多个的。

## 测试

回头发现，其实我们已经开发了相当多的代码，那么是时候来测试一下了，在`src/test/java/com/lin/cms/merak/controller/v1`目录下
我们新建一个`BookControllerTest.java`:

```java
package com.lin.cms.merak.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
@AutoConfigureMockMvc
@Slf4j
class BookControllerTest {

    @Test
    void getBook() {
        
    }

    @Test
    void createBook() {
    }
}
```

我们已经添加上两个测试函数分别对应两个API，首先我们测试一下`getBook`：

```java
package com.lin.cms.merak.controller.v1;

import com.lin.cms.merak.mapper.BookMapper;
import com.lin.cms.merak.model.BookDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
@AutoConfigureMockMvc
@Slf4j
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookMapper bookMapper;

    private Long id;
    private String title = "千里之外";
    private String author = "pedro";
    private String image = "千里之外.png";
    private String summary = "千里之外，是周杰伦和费玉清一起发售的歌曲";

    @Test
    void getBook() throws Exception {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(title);
        bookDO.setAuthor(author);
        bookDO.setImage(image);
        bookDO.setSummary(summary);
        bookMapper.insert(bookDO);

        this.id = bookDO.getId();
        this.mvc.perform(get("/v1/book/" + this.id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.title").value(title));
    }

    @Test
    void createBook() {
    }
}
```

然后，我们运行一下这个测试函数；

```bash
mvn test -Dtest=com.lin.cms.merak.controller.v1.BookControllerTest#getBook -DfailIfNoTests=false
```

如果一切顺利，你将会看到类似如下的输出，当然你的输出可能更加详细：

```bash
[INFO] 
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  13.049 s
[INFO] Finished at: 2020-01-28T16:05:29+08:00
[INFO] ------------------------------------------------------------------------
```

随后，我们也为`createBook`完善一下测试，整体代码如下：

```java
package com.lin.cms.merak.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.lin.cms.merak.dto.book.CreateOrUpdateBookDTO;
import com.lin.cms.merak.mapper.BookMapper;
import com.lin.cms.merak.model.BookDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
@AutoConfigureMockMvc
@Slf4j
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookMapper bookMapper;

    private Long id;
    private String title = "千里之外";
    private String author = "pedro";
    private String image = "千里之外.png";
    private String summary = "千里之外，是周杰伦和费玉清一起发售的歌曲";

    @Test
    void getBook() throws Exception {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(title);
        bookDO.setAuthor(author);
        bookDO.setImage(image);
        bookDO.setSummary(summary);
        bookMapper.insert(bookDO);

        this.id = bookDO.getId();
        this.mvc.perform(get("/v1/book/" + this.id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.title").value(title));
    }

    @Test
    void createBook() throws Exception {
        CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
        dto.setAuthor(author);
        dto.setImage(image);
        dto.setSummary(summary);
        dto.setTitle(title);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(post("/v1/book/")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.message").value("新建图书成功"));
    }
}
```

测试一下：

```bash
mvn test -Dtest=com.lin.cms.merak.controller.v1.BookControllerTest -DfailIfNoTests=false
```

## 总结

本小节，我们一起探讨了信息配置的重要性，完善了代码，且为上一节开发的接口书写了测试。
注意，我们并未详细的教你怎么去写单元测试，而是将单元测试的书写逻辑交给了你，因为很多人
可能不写测试，但我们强烈建议你去写测试，你甚至可以为每个mapper和service都写上单元测试。

最后，我们附上一些参考资料供你阅读。

spring-boot web 测试指南[Testing the Web Layer](https://spring.io/guides/gs/testing-web/)。
好用的测试框架[junit](https://junit.org/junit5/)。

<RightMenu />
