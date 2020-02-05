---
title: 令牌
---

# <H2Icon /> 令牌

## 为何选择 jwt?

lin-cms选择jwt作为用户认证鉴权的核心机制。与cookie-session 的机制相比，jwt 更加轻量
和方便，节约了开发和运维成本。在 jwt[官网](https://jwt.io/introduction/)介绍其两大使
用场景。

- Authorization(授权)：这是 jwt 应用最为广泛的场景。jwt 将数据加密存储，分发给前
  端，前端将其放在特定的 header 字段 中（也有放在 params 和 body 中），服务器收
  到请求后，解析 jwt 判断用户身份，对用户请求进行限权。

- Information Exchange(数据交换): jwt 可以通过公钥和私钥对信息进行加密，双方通信
  后，互得数据。

如果你想了解 jwt 更多的信息和生态请阅读 jwt[官网](https://jwt.io/introduction/)。

## access_token 和 refresh_token

jwt 机制颇为灵活，如Github选择了单令牌机制，lin-cms为了增强用户体验、提高接口安全性，
lin-cms选择了双令牌机制。在双令牌机制中，*access_token*和*refresh_token*是一对相互帮助
的好搭档，用户登陆成功后，服务器会颁发 access_token 和 refresh_token，前端在得到这
两个 token 之后必须**谨慎存储**，它们的作用如下：

- access_token ：用户访问接口，资源的凭证。access_token 十分重要
  ，它是服务器对前端有力控制的唯一途径，其生存周期较短，一般在 2 个小时
  左右，更有甚者，其生命周期只有 15 分钟，在 lin-cms 中默认为 1 个小时。

- refresh_token ：用户重新获得access_token的凭证。refresh_token 的生命周
  期较长，一般为 30 天左右，但 refresh_token 不能被用来用户身份鉴权和获取资源，
  它只能被用来重新获取 access_token。当前端发现 access_token 过期时，会自动通过
  refresh_token 重新获取 access_token。

## 使用

lin-cms 已经默认集成了 jwt 双令牌机制，你可以方便地通过 HTTP 请求来进行令牌的颁布和刷新操作。

### 用户登录获取 access_token、refresh_token

`path`: /cms/user/login

`method`: post

`参数`:

| name     |  说明  |  类型  | 作用     |
| -------- | :----: | :----: | -------- |
| username | 用户名 | string | \*\*\*\* |
| password |  密码  | string | \*\*\*\* |

`结果`:

```json
{
  "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1MzU1MzMyNjMsIm5iZiI6MTUzNTUzMzI2MywianRpIjoiMTlkZWUwNzQtNzUxYi00MjBlLTk3NjAtZDRkMzc3YjdjMjUyIiwiZXhwIjoxNTM1NjE5NjYzLCJpZGVudGl0eSI6InBlZHJvIiwiZnJlc2giOmZhbHNlLCJ0eXBlIjoiYWNjZXNzIn0.9sNmAV5anxY5N1S1kaXzRRpdjzVX3fX6iI0ZjxGiiVs",
  "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1MzU1MzMyNjMsIm5iZiI6MTUzNTUzMzI2MywianRpIjoiYjU0OWIwZGEtMTE3MS00NzJlLWE0MDMtMDFkMGRkZTRjOTYzIiwiZXhwIjoxNTM4MTI1MjYzLCJpZGVudGl0eSI6InBlZHJvIiwidHlwZSI6InJlZnJlc2gifQ.cBnqEBnome-dMFEueQ8oCJfoXX9_mzQJAGjyeq4bYh8"
}
```

`使用`:

在得到令牌后，在HTTP请求的Header中加入`Authorization`字段，字段值为`Bearer`加
上`access_token`，注意两个字段中间必须有一个空格，如下：

```bash
Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1MzU1MzMyNjMsIm5iZiI6MTUzNTUzMzI2MywianRpIjoiMTlkZWUwNzQtNzUxYi00MjBlLTk3NjAtZDRkMzc3YjdjMjUyIiwiZXhwIjoxNTM1NjE5NjYzLCJpZGVudGl0eSI6InBlZHJvIiwiZnJlc2giOmZhbHNlLCJ0eXBlIjoiYWNjZXNzIn0.9sNmAV5anxY5N1S1kaXzRRpdjzVX3fX6iI0ZjxGiiVs
```

服务器会解析该字段并得到用户信息，对用户进行鉴权。

### 通过refresh_token 获取 access_token

`path`: /cms/user/refresh

`method`: get

`参数`: 无

`注意`: 必须在HTTP的Header中加上`Authorization`字段，字段值为 `Bearer ${refresh_token}`。 

`结果`:

```json
{
    "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTc4OTI1NjU3fQ.SkyztGH0P7pH1kvxK60nkU8IGkVXF7YTNd706cW0sXw",
    "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoicmVmcmVzaCIsImV4cCI6MTU4MTUxNDA1N30.RmmAq_irja2TCM3tmpUEuJGDLWDWbPf9K0FRc8FklEY"
}
```

`使用`：

用户访问接口、资源时，必须在HTTP Header中附带`Authorization`字段，字段值为`Bearer ${access_token}`，当通过
refresh_token 获取 access_token 时，应将`Authorization`字段的值替换为`Bearer ${refresh_token}`。如：

```bash
Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1MzU1MzMyNjMsIm5iZiI6MTUzNTUzMzI2MywianRpIjoiYjU0OWIwZGEtMTE3MS00NzJlLWE0MDMtMDFkMGRkZTRjOTYzIiwiZXhwIjoxNTM4MTI1MjYzLCJpZGVudGl0eSI6InBlZHJvIiwidHlwZSI6InJlZnJlc2gifQ.cBnqEBnome-dMFEueQ8oCJfoXX9_mzQJAGjyeq4bYh8
```

服务器会自动解析`Authorization`字段，如 refresh_token 未过期则会颁发新的 access_token。

:::tip

在 lin-cms 的前端框架中，已经默认实现了以上机制，你大可不必自己去实现，当然如果
你想深入了解，也可在阅读本小节后自行尝试。

:::

<RightMenu />