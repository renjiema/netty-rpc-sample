# netty rpc sample
本项目使用netty实现一个简单的rpc框架，主要为学习netty使用。

## 什么是 RPC

RPC（Remote Procedure Call）—远程过程调用，它是一种通过网络从远程计算机程序上请求服务，而不需要了解底层网络技术的协议。比如两个不同的服务 A、B 部署在两台不同的机器上，那么服务 A 如果想要调用服务 B 中的某个方法该怎么办呢？使用 HTTP请求 当然可以，但是可能会比较慢而且一些优化做的并不好。 RPC 的出现就是为了解决这个问题。

最终解决的问题：**让分布式或者微服务系统中不同服务之间的调用像本地调用一样简单**。

### 业界常用的 RPC 框架

- **Dubbo:** Dubbo 是阿里巴巴公司开源的一个高性能优秀的服务框架，使得应用可通过高性能的 RPC 实现服务的输出和输入功能，可以和 Spring框架无缝集成。目前 Dubbo 已经成为 Spring Cloud Alibaba 中的官方组件。
- **gRPC** ：gRPC 是可以在任何环境中运行的现代开源高性能RPC框架。它可以通过可插拔的支持来有效地连接数据中心内和跨数据中心的服务，以实现负载平衡，跟踪，运行状况检查和身份验证。
- **Hessian：** Hessian是一个轻量级的 remoting-on-http 工具，使用简单的方法提供了 RMI 的功能。 相比 WebService，Hessian 更简单、快捷。采用的是二进制 RPC协议，因为采用的是二进制协议，所以它很适合于发送二进制数据。

## 实现RPC框架需要做什么

完整的 RPC 实现一般会包含有 **传输协议** 和 **序列化协议**，因此要实现RPC框架肯定要从这两方面开始。

### 传输协议

我们熟悉的HTTP就是一种传输协议，RPC 框架可以使用 HTTP 作为传输协议，也可以直接使用 TCP，使用不同的协议一般也是为了适应不同的场景。

使用 TCP 和使用 HTTP 各有优势：

**传输效率**：

- TCP，位于网络层，通常自定义上层协议，可以让请求报文体积更小
- HTTP：位于应用层，如果是基于HTTP 1.1 的协议，请求中会包含很多无用的内容

**性能消耗**，主要在于序列化和反序列化的耗时

- TCP，可以基于各种序列化框架进行，效率比较高
- HTTP，大部分是通过 json 来实现的，字节大小和序列化耗时都要更消耗性能

**跨平台**：

- TCP：通常要求客户端和服务器为统一平台
- HTTP：可以在各种异构系统上运行

**总结**：
  RPC 的 TCP 方式主要用于公司内部的服务调用，性能消耗低，传输效率高。HTTP主要用于对外的异构环境，浏览器接口调用，APP接口调用，第三方接口调用等。

### 序列化协议

序列化与反序列化是开发过程中不可或缺的一步，简单来说，序列化是将对象转换成字节流的过程，而反序列化的是将字节流恢复成对象的过程。

序列化与序列化主要解决的是数据的一致性问题。简单来说，就是输入数据与输出数据是一样的。对于数据的本地持久化，只需要将数据转换为字符串进行保存即可是实现，但对于远程的数据传输，由于操作系统，硬件等差异，会出现内存大小端，内存对齐等问题。

常见的序列化协议有：XML(Extensible Markup Language)、JSON(JavaScript Object Notation)、Protobuf等，不同的序列化协议可以从性能，数据大小，可读性三方面进行比较，一般可读性都会影响性能和数据大小。

除此之外还有Java自带的序列化方式和Dubbo采用的Hessian协议。

### 常用RPC框架的实现

* Dubbo协议，Dubbo框架使用的协议，使用Hessian二进制序列化，并基于TCP实现传输协议，具体的传输协议如下：

![](https://pic1.zhimg.com/80/v2-8db163dc36a973358307d3c78a3016cc_720w.jpg)

  - Magic - Magic High & Magic Low (16 bits)：标识协议版本号，Dubbo 协议：0xdabb

  - Req/Res (1 bit)：标识是请求或响应。请求： 1; 响应： 0。

  - 2 Way (1 bit)：仅在 Req/Res 为1（请求）时才有用，标记是否期望从服务器返回值。如果需要来自服务器的返回值，则设置为1。

  - Event (1 bit)：标识是否是事件消息，例如，心跳事件。如果这是一个事件，则设置为1。

  - Serialization ID (5 bit)：标识序列化类型：比如 fastjson 的值为6。

  - Status (8 bits)：仅在 Req/Res 为0（响应）时有用，用于标识响应的状态。

  - - 20 - OK
    - 30 - CLIENT_TIMEOUT
    - 31 - SERVER_TIMEOUT
    - 40 - BAD_REQUEST
    - 50 - BAD_RESPONSE
    - 60 - SERVICE_NOT_FOUND
    - 70 - SERVICE_ERROR
    - 80 - SERVER_ERROR
    - 90 - CLIENT_ERROR
    - 100 - SERVER_THREADPOOL_EXHAUSTED_ERROR

  

  - Request ID (64 bits)：标识唯一请求。类型为long。

  - Data Length (32 bits)：序列化后的内容长度（可变部分），按字节计数。int类型。

  - Variable Part：被特定的序列化类型（由序列化 ID 标识）序列化后，每个部分都是一个 byte [] 或者 byte

  - - 如果是请求包 ( Req/Res = 1)，则每个部分依次为：

    - - Dubbo version
      - Service name
      - Service version
      - Method name
      - Method parameter types
      - Method arguments
      - Attachments

    - 如果是响应包（Req/Res = 0），则每个部分依次为：

    - - 返回值类型(byte)，标识从服务器端返回的值类型：

      - - 返回空值：RESPONSE_NULL_VALUE 2
        - 正常响应值： RESPONSE_VALUE 1
        - 异常：RESPONSE_WITH_EXCEPTION 0

  - - - 返回值：从服务端返回的响应bytes

* gRPC协议，gRPC协议使用Protobuf实现序列化，基于HTTP/2实现输出协议，具体的实现后续有时间再补充。

## 自定义传输协议

因为TCP/IP 中消息传输基于流的方式，没有边界，因此会出现[粘包于粘包的现象](https://github.com/renjiema/note/blob/bc2fecc8cff3a3bf78bff317cb0f7f52a0e7999d/java/netty/Netty.md#L1)，所以必须定义协议划定消息的边界。

### 自定义协议要素

* 魔数，用来在第一时间判定是否是无效数据包
* 版本号，可以支持协议的升级
* 序列化算法，消息正文到底采用哪种序列化反序列化方式，可以由此扩展，例如：json、protobuf、hessian、jdk
* 指令类型，是登录、注册、单聊、群聊... 跟业务相关
* 请求序号，为了双工通信，提供异步能力
* 正文长度
* 消息正文





参考

[知乎回答](https://www.zhihu.com/question/25536695/answer/1846152026)

