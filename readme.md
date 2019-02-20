# 秒杀项目


## 架构

## 目录

github仓库: git@github.com:Xia-Ao/miaosha.git

分支：
* `master`：重构springBoot代码
* `source`：springBoot项目在github上的源码参考
* `view`： 基于前端UI框架的html页面，框架static目录在本地


## 技术栈 & 开发细节
### 定义通用返回对象
对错误，异常，正确的返回结果进行处理，统一返回给前端可用的信息而不是500错误

1. 定义通用错误接口CommonError
2. 定义错误类型枚举EmBusinessErr 实现CommonError接口
3. 定义一个错误异常类BusinessException， 继承Exception类，并实现CommonError通过错误接口
4. 定义个通用的返回类CommonReturnType 设置返回status和data格式的返回类型
5. 定义一个基本的错误处理handler，用来处理未被controller吸收的Exception异常，将异常交给handler处理，在handler中捕获Exception然后调用上面的几个类，得到对应的返回的错误码和错误提示，再调用对应的CommonReturnType返回status-data格式的返回结果。
6. 将这个异常捕获返回的handler，创建为一个BaseController, 在每一个Controller中继承他。


### 注册登录模块
这次单独把这个注册登录拿出来讲，其实对弈一个服务来讲，最基本的就是注册登录，每一次在写注册登录的时候，思想是很简单，
校验注册字段，校验通过插入数据库，这个项目讲到了一个严谨的系统，需要将账号信息，和密码分开存储，密码还应该加密
创建不同的userModel，分别返回给用户。

在用户校验中，使用validate类进行校验是比较合理的方式，不要在一个一个的对参数进行判断处理


### 开发模式
每设计到一个功能的时候，
1. 先创建该功能的数据模型，例如商品数据模型itemModel，用户数据模型userModel，订单数据模型orderModel，活动数据模型promoModel，
2. 再根据数据模型，生成对应的数据库表，
3. 利用mybatis-generator自动生成工具生成dao层和mapper，这样数据层就创建完成
4. 根据功能需求，实现service和serviceImpl，这里面有对应的业务逻辑，并且中间操作数据传递的数据和通过数据库获取的数据都是Do类，需要将Do和Model类数据进行转换，保证数据分层
5. 再创建Controller，对应不同的接口，以及调用服务来满足接口处理，
6. 关于返回给前端数据，在前面讲到过，定义通过返回对象，数据正确返回数据，处理错误返回错误原因。返回给前端的数据不能是直接Model类，有时候这些Model数据和前端需要的数据一样，但是也应该做区分，创建一个vo类，根据前端需要设计字段，再将Model数据复制给Vo

这样的开发模式，思路很清晰，每一步知道自己做什么，为什么这样做，不会这里写一下，意识到之前的代码有问题，然后又回头改之前的代码，然后再回来改，一来二去，自己也不知道道理该怎么设计。

## 工具类

### joda time
[简介](https://www.ibm.com/developerworks/cn/java/j-jodatime.html)

比源生util.Date更好用的时间工具


