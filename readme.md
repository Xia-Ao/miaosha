# 秒杀项目


## 架构

## 目录


## 技术栈 & 开发细节
### 定义通用返回对象
对错误，异常，正确的返回结果进行处理，统一返回给前端可用的信息而不是500错误

1. 定义通用错误接口CommonError
2. 定义错误类型枚举EmBusinessErr 实现CommonError接口
3. 定义一个错误异常类BusinessException， 继承Exception类，并实现CommonError通过错误接口
4. 定义个通用的返回类CommonReturnType 设置返回status和data格式的返回类型
5. 定义一个基本的错误处理handler，用来处理未被controller吸收的Exception异常，将异常交给handler处理，在handler中捕获Exception然后调用上面的几个类，得到对应的返回的错误码和错误提示，再调用对应的CommonReturnType返回status-data格式的返回结果。


### 注册登录模块
这次单独把这个注册登录拿出来讲，其实对弈一个服务来讲，最基本的就是注册登录，每一次在写注册登录的时候，思想是很简单，
校验注册字段，校验通过插入数据库，这个项目讲到了一个严谨的系统，需要将账号信息，和密码分开存储，密码还应该加密
创建不同的userModel，分别返回给用户。

在用户校验中，使用validate类进行校验是比较合理的方式，不要在一个一个的对参数进行判断处理


## 工具类

### joda time
[简介](https://www.ibm.com/developerworks/cn/java/j-jodatime.html)

比源生util.Date更好用的时间工具


