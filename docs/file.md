---
title: 文件上传
---

# 文件上传

lin-cms默认集成了文件上传功能，前端也有相应的组件匹配使用。

## 使用

lin-cms默认对外暴露了`cms/file/`作为文件上传接口，通过该接口可以直接使用
HTTP post 方法上传文件。
 
lim-cms默认将文件上传到本地；lin-cms是通过[扩展](./extension.md)来实现
文件上传的，代码结构如下（主要功能已写在了注释中）：

```bash
├── File.java // File容器类，即Data Object，存储文件信息
├── FileConsts.java // File常量类
├── FileProperties.java // File配置装载类
├── FileUtil.java // 工具类
├── LocalUploader.java // 默认的本地实现
├── PreHandler.java // 上传之前接口调用
├── Uploader.java // 上传接口
└── config.properties // 配置文件
```

lin-cms将文件上传定义为了一个通用接口`Uploader.java`：

```java
public interface Uploader {

    /**
     * 上传文件
     *
     * @param fileMap 文件map
     * @return 文件数据
     */
    List<File> upload(MultiValueMap<String, MultipartFile> fileMap);

    /**
     * 上传文件
     *
     * @param fileMap    文件map
     * @param preHandler 预处理器
     * @return 文件数据
     */
    List<File> upload(MultiValueMap<String, MultipartFile> fileMap, PreHandler preHandler);
}
```

为了保证`Uploader`的通用性，该接口默认有两个`upload`方法。

`fileMap`参数是文件容器，从中可以拿到前端上传文件的数据和信息。

`preHandler`是一个特殊的参数，它也是一个接口，从名称上可以看出它是一个
前置处理器，会在文件数据写入到本地或者上传到OSS之前调用，如下：

```java
public interface PreHandler {

    /**
     * 在文件写入本地或者上传到云之前调用此方法
     *
     * @return 是否上传，若返回false，则不上传
     */
    boolean handle(File file);
}
```

通过自定义PreHandler，你可以通过判断`File`中的信息来决定该文件是否要写入到
本地或者上传到云。

`LocalUploader`是lin-cms提供的`Upload`默认实现，它会将前端上传的文件存储到
本地；当然一般情况下，会存储到云上，你完全可以自己来实现`Upload`来达到你的
目的，lin-cms后续也会开发其它实现，如上传到阿里云。

## 配置

在具体的实践之前，我们需要了解一下文件上传提供的配置。

```properties
# upload
# 只能从max-file-size设置总体文件的大小
# 上传文件总大小
spring.servlet.multipart.max-file-size=20MB
# 每个文件的大小
lin.cms.file.single-limit=2MB
# 上传文件总数量
lin.cms.file.nums=10
# 禁止某些类型文件上传，文件格式以,隔开
lin.cms.file.exclude=
# 允许某些类型文件上传，文件格式以,隔开
lin.cms.file.include=.jpg,.png,.jpeg
# 文件上传后，访问域名配置
lin.cms.file.domain=http://localhost:5000/
# 文件存储位置，默认在工作目录下的assets目录
lin.cms.file.store-dir=assets/
```

在每个配置的后面，我们均以注释的方式解释了每项的作用。

当然还需要着重解释一下`exclude`和`include`这两项配置，默认情况下，这两者只会有一项生效；
若这二者中有一项为空，则另一项不为空的配置会生效；
如果两项皆为空的话，会通过所有文件类型；
如果二者都不为空，则`include`为有效配置，而`exclude`会失效；
总而言之，系统只会支持使用一项，二者都为为空的情况下，则通过所有文件类型。

## 实践

使用 postman 测试一下文件上传：

<img-wrapper>
  <img src="http://imglf4.nosdn0.126.net/img/Qk5LWkJVWkF3NmlvOHFlZzFHSk95OGhiL0lSSFo3OFNPSGc1WEFnc0JRVERUb2JSU0cvSUlnPT0.png?imageView&thumbnail=2090y1120&type=png&quality=96&stripmeta=0">
</img-wrapper>

上传成功后，我们会得到如下的结果：

```
[
  {
    key: 'one',
    id: 13,
    url:
      'http://localhost:5000/assets/2019/05/19/3428c02f-dfb5-47a0-82cf-2d6c05285473.png'
  },
  {
    key: 'two',
    id: 14,
    url:
      'http://localhost:5000/assets/2019/05/19/bfcff6f4-8dd7-4dd8-af9d-660781d0e076.jpg'
  }
];
```

由于上传了两个文件，因此我们得到了两个元素的数组，它们的结构如下：

| name |         说明          |  类型  |
| ---- | :------------------: | :----: |
| key  |    文件上传的 key     | string |
| id   | 文件存储到数据库的 id  | string |
| url  |     可访问的 url      | string |

:::tip

系统会自动帮助我们上传的文件做 md5，因此你大可不必担心文件重复上传，如果你上传了
重复的文件，服务器会返回已传文件的数据。

:::

<RightMenu />
