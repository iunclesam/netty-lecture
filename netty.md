#  学习的要义

## 关键点

- 强势技能的学习
- 重视基础
- 节奏感
- 思维方式的改进
- 效率工具的使用
- 主动输出

# 精通并发与Netty

## 课程大纲

- Netty介绍
- Netty架构实现
- Netty模块分析
- Netty HTTP Tunnel
- Netty对Scoket的实现
- Netty压缩与解压缩
- Netty对于RPC的支援
- WebSocket实现与原理分析
- WebSocket连接建立方式与生命周期分解
- WebSocket服务端与客户端开发
- RPC框架分析
- Google Protobuf使用方式分析
- Apache Thrift使用方式与文件编写方式分析
- Netty大文件传送支持
- 可扩展的事件模型
- Netty统一通信API
- 零拷贝在Netty中的实现与支持
- TCP粘包与拆包分析
- NIO模型在Netty中的实现
- Netty编解码开发技术
- Netty重要类与接口源代码剖析
- Channel分析
- 序列化讲解



## socket协议

ws://server:port/context_path	ws://localhost:8899/ws



rmi：remote method invocation 只针对Java

client：stub

server：skeleton

序列化与反序列化 也叫做编码与解码



RPC：Remote Procedure Call，远程过程调用，很多RPC框架是跨语言的。

1. 定义一个接口说明文件：描述了对象（结构体）、对象成员、接口方法等一系列信息。
2. 通过RPC框架所提供的编译器，将接口说明文件编译成具体语言文件。
3. 在客户端与服务器端分别引入RPC编译器所生成的文件，即可像调用本地方法一样调用远程方法



## protocol-buffers

https://developers.google.com/protocol-buffers/

1. 下载编译器
    https://github.com/protocolbuffers/protobuf/releases/download/v3.10.0/protoc-3.10.0-osx-x86_64.zip

2. 配置环境

   ```shell
   vi .zshrc
   export PATH=$HOME/bin:/usr/local/bin:/Users/agan/software/protoc-3.10.0-osx-x86_64/bin:$PATH
   ```

3. 编写Student.proto

   ```protobuf
   syntax = "proto2";
   
   package com.shengsiyuan.protobuf;
   
   option optimize_for = SPEED;
   option java_package = "com.shengsiyuan.protobuf";
   option java_outer_classname = "DataInfo";
   
   message Student {
     required string name = 1;
     optional int32 age = 2;
     optional string address = 3;
   }
   ```

4. 编译

   ```shell
   netty-lecture git:(master) ✗ protoc --java_out=src/main/java src/main/protobuf/Student.proto
   ```

## thrift

### 安装

```shell
brew install thrift
```



- Thrift最初由Facebook研发，主要用于各服务之间RPC通信，支持跨语言，常用的语言比如C++，Java，Python，PHP，Ruby，Erlang，Perl，Haskell，C#，Cocoa，JavaScript，Node.js，Smalltalk，and OCaml都支持。
- Thrift是一个典型的CS（客户端/服务端）结构，客户端和服务端可以使用不同的语言开发。既然客户端和服务端能使用不同的语言开发，那么一定就要有一种中间语言来关联客户端和服务端的语言，这种语言就是IDL（Interface Description Language）

### Thrift数据类型

- Thrift不支持无符号类型，因为很多编程语言不存在无符号类型，比如Java
- byte：有符号字节
- i16：16位有符号整数
- i32：32位有符号整数
- i64：64位有符号整数
- double：64位浮点数
- string：字符串

### Thrift容器类型

- 集合中的元素可以是除了service之外的任何类型，包括exception
- list：一系列由T类型的数据组成的有序列表，元素可以重复
- set：一系列由T类型的数据组成的有序列表，元素可以重复
- map：一个字典结构，key为K类型，value为V类型，相当于Java中的HashMap

### Thrift工作原理

- 如何实现多语言之间的通信？
  - 数据传输使用socket（多语言均支持），数据再以特定的格式（String等）发送，接收方语言进行解析
  - 定义thrift的文件，由thrift文件（IDL）生成双方语言的接口、model，在生成的model以及接口中会有解码编码的代码

### 结构体（struct）

- 就像C语言一样，Thrift支持struct类型，目的就是将一些数据聚合在一起，方便传输管理。struct的定义形式如下：

  ```c
  struct People {
      1:string name;
      2:i32 age;
      3:string gender;
  }
  ```

### 枚举（enum）

- 枚举的定义形式和Java的Enum定义类似：

  ```c
  enum Gender {
      MALE,
      FEMALE
  }
  ```

### 异常（exception）

- Thrift支持自定义exception，规则与struct一样

  ```thri
  exception RequestException {
      1:i32 code;
      2:string reason;
  }
  ```

### 服务（service）

- Thrift定义服务相当于Java中创建Interface一样，创建的service经过代码生成命令之后就会生成客户端和服务端的框架代码。定义形式如下：

  ```thrift
  // service中定义的函数，相当于Java interface定义的方法
  service HelloWorldService {
      string doAction(1:string name,2:i32 age);
  }
  ```

###类型定义

- Thrift支持类似C++一样的typedef定义

  ```c++
  typedef i32 int
  typedef i64 long
  ```

### 常量

- thrift也支持常量定义，使用const关键字

  ```c++
  const i32 MAX_RETRIES_TIME = 10
  const string MY_WEBSITE = "http://facebook.com"
  ```

### 命名空间

- Thrift的命名空间相当于Java中的package的意思，主要目的是组织代码。thrift使用关键字namespace定义命名空间：

- 格式是：namespace 语言名 路径

  *namespace java com.test.thrift.demo*

### 文件包含

- Thrift也支持文件包含，相当于C/C++中的include，Java中的import。使用关键字include定义：

  *include "global.thrift"*

### 注释

- Thrift注释方式支持shell风格的注释，支持C/C++风格的注释，即#和//开头的语句都当做注释，/**/包裹的语句也是注释。

### 可选与必选

- Thrift提供两个关键字required，optional，分别用于表示对于的字段是必填的还是可选的

  ```c
  struct People {
      1:required string name;
      2:optional i32 age;
  }
  ```

### 生成代码

- 了解了如何定义thrift文件之后，我们需要用定义好的thrift文件生成我们需要的目标语言的源码

- 首先需要定义thrift接口描述文件

- 参见data.thrift

  ```shell
  thrift --gen java src/thrift/data.thrift
  ```

### 服务端实现

```java
package com.shengsiyuan.thrift;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import thrift.generated.PersonService;

public class ThriftServer {

    public static void main(String[] arg) throws Exception {
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        THsHaServer.Args args = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        args.protocolFactory(new TCompactProtocol.Factory());
        args.transportFactory(new TFramedTransport.Factory());
        args.processorFactory(new TProcessorFactory(processor));

        TServer server = new THsHaServer(args);

        System.out.println("Thrift Server Started!");

        server.serve();
    }
}
```

### 客户端实现

```java
package com.shengsiyuan.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import thrift.generated.Person;
import thrift.generated.PersonService;

public class ThriftClient {
    public static void main(String[] args) {
        TTransport transport = new TFramedTransport(new TSocket("127.0.0.1", 8899), 6000);
        TProtocol protocol = new TCompactProtocol(transport);
        PersonService.Client client = new PersonService.Client(protocol);

        try {
            transport.open();

            Person person = client.getPersonByUsername("张三");

            System.out.println(person.getUsername());
            System.out.println(person.getAge());
            System.out.println(person.isMarried());

            System.out.println("----------");

            Person person2 = new Person();
            person2.setUsername("李四");
            person2.setAge(30);
            person2.setMarried(true);

            client.savePerson(person2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
    }
}
```

```gra
dependencies {
    compile (
            "org.apache.thrift:libthrift:0.12.0"
    )
}
```

### Thrift架构

![必应图片](<http://videos.web-03.net/artigos/Leonardo_Gocalves_Da_Silva/Apache_Camel_Thrift/Apache_Camel_Thrift2.jpg>)

### Thrift传输格式

- TBinaryProtocol-二进制格式
- TCompactProtocol-压缩格式
- TJSONProtocol-JSON格式
- TSimpleJSONProtocol-提供JSON只写协议，生成的文件很容易通过脚本语言解析。
- TDebugProtocol-使用易懂的可读的文本格式，以便于debug，当前无Java实现。

### Thrift数据传输方式

- TSocket-阻塞式socket
- TFramedTransport-以frame为单位进行传输，非阻塞式服务中使用
- TFileTransport-以文件形式进行传输
- TMemoryTransport-将内存用于I/O，Java实现时内部实际使用了简单的ByteArrayOutputStream
- TZlibTransport-使用zlib进行压缩，与其他传输方式联合使用。

### Thrift支持的服务模型

- TSimpleServer-简单的单线程服务模型，常用于测试
- TThreadPoolServer-多线程服务模型，使用标准的阻塞式IO
- TNonblockingServer-多线程服务模型，使用非阻塞式IO（需使用TFramedTransport数据传输方式）
- THsHaServer-THsHa引入了线程池去处理，其他模型把读写任务放到线程池去处理；Half-sync/Half-async的处理模式，Half-async是在处理IO事件上（accpet/read/write io）,Half-sync用于handler对rpc的同步处理

### Thrift对多语言的支持

- Java

  - data.thrift

    ```thrift
    namespace java thrift.generated
    namespace py py.thrift.generated
    
    typedef i16 short
    typedef i32 int
    typedef i64 long
    typedef bool boolean
    typedef string String
    
    struct Person {
        1: optional String username;
        2: optional int age;
        3: optional boolean married;
    }
    
    exception DataException {
        1: optional String message;
        2: optional String callStack;
        3: optional String data;
    }
    
    service PersonService {
        Person getPersonByUsername(1: required String username) throws (1: DataException dataException),
        void savePerson(1: required Person person) throws (1: DataException dataException)
    }
    ```

  - 通过data.thrift生成Java代码

    ```
    thrift --gen java src/thrift/data.thrift
    ```

  - ThriftServer.java

    ```java
    package com.shengsiyuan.thrift;
    
    import org.apache.thrift.TProcessorFactory;
    import org.apache.thrift.protocol.TCompactProtocol;
    import org.apache.thrift.server.THsHaServer;
    import org.apache.thrift.server.TServer;
    import org.apache.thrift.transport.TFramedTransport;
    import org.apache.thrift.transport.TNonblockingServerSocket;
    import com.shengsiyuan.thrift.generated.PersonService;
    
    public class ThriftServer {
    
        public static void main(String[] arg) throws Exception {
            TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
            THsHaServer.Args args = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
            PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());
    
            args.protocolFactory(new TCompactProtocol.Factory());
            args.transportFactory(new TFramedTransport.Factory());
            args.processorFactory(new TProcessorFactory(processor));
    
            TServer server = new THsHaServer(args);
    
            System.out.println("Thrift Server Started!");
    
            server.serve();
        }
    }
    ```

    ```java
    package com.shengsiyuan.thrift;
    
    import org.apache.thrift.TException;
    import com.shengsiyuan.thrift.generated.DataException;
    import com.shengsiyuan.thrift.generated.Person;
    import com.shengsiyuan.thrift.generated.PersonService;
    
    public class PersonServiceImpl implements PersonService.Iface {
        @Override
        public Person getPersonByUsername(String username) throws DataException, TException {
            System.out.println("Got Client Param：" + username);
            Person person = new Person();
            person.setUsername(username);
            person.setAge(20);
            person.setMarried(false);
            return person;
        }
    
        @Override
        public void savePerson(Person person) throws DataException, TException {
            System.out.println("Got Client Param：");
            System.out.println(person.getUsername());
            System.out.println(person.getAge());
            System.out.println(person.isMarried());
        }
    }
    ```

  - ThriftClient.java

    ```java
    package com.shengsiyuan.thrift;
    
    import org.apache.thrift.protocol.TCompactProtocol;
    import org.apache.thrift.protocol.TProtocol;
    import org.apache.thrift.transport.TFramedTransport;
    import org.apache.thrift.transport.TSocket;
    import org.apache.thrift.transport.TTransport;
    import com.shengsiyuan.thrift.generated.Person;
    import com.shengsiyuan.thrift.generated.PersonService;
    
    public class ThriftClient {
        public static void main(String[] args) {
            TTransport transport = new TFramedTransport(new TSocket("127.0.0.1", 8899), 6000);
            TProtocol protocol = new TCompactProtocol(transport);
            PersonService.Client client = new PersonService.Client(protocol);
    
            try {
                transport.open();
    
                Person person = client.getPersonByUsername("张三");
    
                System.out.println(person.getUsername());
                System.out.println(person.getAge());
                System.out.println(person.isMarried());
    
                System.out.println("----------");
    
                Person person2 = new Person();
                person2.setUsername("李四");
                person2.setAge(30);
                person2.setMarried(true);
    
                client.savePerson(person2);
    
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                transport.close();
            }
        }
    }
    ```

- [Python](<https://github.com/iunclesam/pythrift>)

  - 下载 http://mirrors.tuna.tsinghua.edu.cn/apache/thrift/0.12.0/thrift-0.12.0.tar.gz

  - 进入thrift包目录，执行如下命令生成Python依赖库

    ```bash
    cd thrift-0.12.0/lib/py
    sudo python setup.py install
    ```

  - 新建项目~/work/ pythrift

  - 选择依赖库

    ```
    /Library/Python/2.7/site-packages/six-1.12.0-py2.7.egg
    ```

  - 通过data.thrift生成Python代码

    ```
    thrift --gen py src/thrift/data.thrift
    ```

  - 生成代码 copy 至 pythrift项目目录下

  - py_client.py

    ```python
    # _*_ coding:utf-8 _*_
    __author__ = '作者'
    
    from py.thrift.generated import PersonService
    from py.thrift.generated import ttypes
    
    from thrift import Thrift
    from thrift.transport import TSocket
    from thrift.transport import TTransport
    from thrift.protocol import TCompactProtocol
    import sys
    
    reload(sys)
    sys.setdefaultencoding('utf8')
    
    try:
        tSocket = TSocket.TSocket('127.0.0.1', 8899)
        tSocket.setTimeout(600)
    
        transport = TTransport.TFramedTransport(tSocket)
        protocol = TCompactProtocol.TCompactProtocol(transport)
        client = PersonService.Client(protocol)
    
        transport.open()
    
        person = client.getPersonByUsername('张三')
    
        print person.username
        print person.age
        print person.married
    
        print '---------------'
    
        person2 = ttypes.Person()
        person2.username = '李四'
        person2.age = 33
        person2.married = True
    
        client.savePerson(person2)
    
        transport.close()
    
    except Thrift.TException, tx:
        print '%s' % tx.message
    ```

  - py_server.py

    ```python
    # _*_ coding:utf-8 _*_
    __author__ = '作者'
    
    from py.thrift.generated import PersonService
    from PersonServiceImpl import PersonServiceImpl
    
    from thrift import Thrift
    from thrift.transport import TTransport
    from thrift.transport import TSocket
    from thrift.protocol import TCompactProtocol
    from thrift.server import TServer
    
    try:
        personServiceHandler = PersonServiceImpl()
        processor = PersonService.Processor(personServiceHandler)
    
        serverSocket = TSocket.TServerSocket(port=8899)
        transportFactory = TTransport.TFramedTransportFactory()
        protocolFactory = TCompactProtocol.TCompactProtocolFactory()
    
        server = TServer.TSimpleServer(processor, serverSocket, transportFactory, protocolFactory)
        server.serve()
    except Thrift.TException, ex:
        print '%s' % ex.message
    ```

    ```python
    # _*_ coding:utf-8 _*_
    __author__ = '作者'
    
    from py.thrift.generated import ttypes
    
    class PersonServiceImpl:
    
        def getPersonByUsername(self, username):
            print 'Got client param: ' + username
    
            person = ttypes.Person();
            person.username = username
            person.age = 20
            person.married = False
    
            return person
        def savePerson(self, person):
            print 'Got client param: '
    
            print person.username
            print person.age
            print person.married
    ```

## gradle

### 编译

 ```shell
gradle clean build 
 ```

### gradlew

- 命令

  ```shell
  gradle wrapper
  ```

- 生成以下文件

  ├── gradle 
  │   └── wrapper
  │       ├── gradle-wrapper.jar
  │       └── gradle-wrapper.properties
  ├── gradlew 
  ├── gradlew.bat

- gradle-wrapper.properties

- ```properties
  distributionBase=GRADLE_USER_HOME
  distributionPath=wrapper/dists
  distributionUrl=https\://services.gradle.org/distributions/gradle-3.5-bin.zip
  zipStoreBase=GRADLE_USER_HOME
  zipStorePath=wrapper/dists
  ```

-  ./gradlew clean build （等价于 gradle clean build）

- ```bash
  pwd
  /Users/agan/.gradle/wrapper/dists
  ls
  gradle-3.5-bin      gradle-3.5-rc-2-bin gradle-4.10.3-bin   gradle-5.4.1-bin
  ```

- gradle wrapper --gradle-version 4.10.3

- build.gradle

  ```groovy
  plugins {
      id 'java'
  }
  
  group 'com.shengsiyuan.gradlew'
  version '1.0-SNAPSHOT'
  
  sourceCompatibility = 1.8
  targetCompatibility = 1.8
  
  repositories {
      mavenCentral()
  }
  
  dependencies {
      testCompile group: 'junit', name: 'junit', version: '4.12'
  }
  
  task wrapper(type: Wrapper) {
      gradleVersion = '3.4'
      distributionType = 'all'
  }
  ```

## grpc

### Java

- 安装插件 build.gradle

  ```groovy
  apply plugin: 'com.google.protobuf'
  
  sourceSets {
      main {
          proto {
              srcDir 'src/protobuf'
          }  
      }
  }
  
  protobuf {
  
      protoc {
          artifact = "com.google.protobuf:protoc:3.2.0"
      }
      
      plugins {
          grpc {
              artifact = 'io.grpc:protoc-gen-grpc-java:1.4.0'
          }
      }
  
      generatedFilesBaseDir = 'src'
  
      generateProtoTasks {
  
          all()*.plugins {
              grpc {
                  setOutputSubDir 'java'
              }
          }
      }
  }
  ```

- 编写IDL Student.proto

  ```groovy
  syntax = "proto3";
  
  package com.shengsiyuan.proto;
  
  option java_package = "com.shengsiyuan.proto";
  option java_outer_classname = "StudentProto";
  option java_multiple_files = true;
  
  service StudentService {
      rpc GetRealNameByUserName(MyRequest) returns (MyResponse) {}
  }
  
  message MyRequest {
      string username = 1;
  }
  
  message MyResponse {
      string realname = 2;
  }
  ```

- 代码生成 

  ```bash
  gradle generateProto
  ```

- 服务端 GrpcServer.java

  ```java
  package com.shengsiyuan.grpc;
  
  import com.shengsiyuan.proto.*;
  import io.grpc.stub.StreamObserver;
  
  public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {
      @Override
      public void getRealNameByUserName(MyRequest request, StreamObserver<MyResponse> responseObserver) {
          System.out.println("接收到客户端信息：" + request.getUsername());
          responseObserver.onNext(MyResponse.newBuilder().setRealname("张三").build());
          responseObserver.onCompleted();
      }
  }
  ```

- StudentServiceImpl.java

  ```java
  package com.shengsiyuan.grpc;
  
  import com.shengsiyuan.proto.*;
  import io.grpc.stub.StreamObserver;
  
  public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {
      @Override
      public void getRealNameByUserName(MyRequest request, StreamObserver<MyResponse> responseObserver) {
          System.out.println("接收到客户端信息：" + request.getUsername());
          responseObserver.onNext(MyResponse.newBuilder().setRealname("张三").build());
          responseObserver.onCompleted();
      }
  }
  
  ```

- 客户端 GrpcClient.java

- ```java
  package com.shengsiyuan.grpc;
  
  import com.shengsiyuan.proto.*;
  import io.grpc.ManagedChannel;
  import io.grpc.ManagedChannelBuilder;
  import java.util.Iterator;
  
  public class GrpcClient {
      public static void main(String[] args) {
          ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8899)
                  .usePlaintext(true).build();
          StudentServiceGrpc.StudentServiceBlockingStub blockingStub =
                  StudentServiceGrpc.newBlockingStub(managedChannel);
                  
          MyResponse myResponse = blockingStub.getRealNameByUserName(MyRequest.newBuilder().setUsername("zhangsan").build());
          System.out.println(myResponse.getRealname());
      }
  }
  ```

### Node

动态代码生成

- 新建项目 ~/work/front_end/grpc_demo
- [package.json](https://github.com/grpc/grpc/blob/v1.4.x/examples/node/package.json)

- ```json
  {
    "name": "grpc-examples",
    "version": "0.1.0",
    "dependencies": {
      "async": "^1.5.2",
      "google-protobuf": "^3.0.0",
      "grpc": "^1.0.0",
      "lodash": "^4.6.1",
      "minimist": "^1.2.0"
    }
  }
  ```

- npm install

- proto/Student.proto

- grpcClient.js

- ```javascript
  var PROTO_FILE_PATH = '/Users/agan/work/front_end/grpc_demo/proto/Student.proto';
  
  var grpc = require('grpc');
  
  var grpcService = grpc.load(PROTO_FILE_PATH).com.shengsiyuan.proto;
  
  var client = new grpcService.StudentService('127.0.0.1:8899', grpc.credentials.createInsecure());
  
  client.getRealNameByUserName({username: '李四'}, function (error, respData) {
      console.log(respData)
  })
  ```

- grpcServer.js

- ```javascript
  var PROTO_FILE_PATH = '/Users/agan/work/front_end/grpc_demo/proto/Student.proto';
  
  var grpc = require('grpc');
  
  var grpcService = grpc.load(PROTO_FILE_PATH).com.shengsiyuan.proto;
  
  var server = new grpc.Server();
  
  server.addService(grpcService.StudentService.service, {
      getRealNameByUserName: getRealNameByUserName,
      getStudentByAge: getStudentByAge,
      getStudentWrapperByAges: getStudentWrapperByAges,
      biTalk: biTalk
  });
  
  server.bind('127.0.0.1:8899', grpc.ServerCredentials.createInsecure());
  server.start();
  
  function getRealNameByUserName(call, callback) {
      console.log('username：' + call.request.username);
      callback(null, {realname: 'node-张三'});
  }
  
  function getStudentByAge() {   
  }
  
  function getStudentWrapperByAges() {
  }
  
  function biTalk() { 
  }
  ```

- 启动服务端 

  ```bash
  node app/grpcServer.js
  ```

- 启动客户端 

  ```bash
  node app/grpcClient.js 
  ```

静态代码生成

- 代码生成

  ```bash
  npm install -g grpc-tools
  
  grpc_tools_node_protoc --js_out=import_style=commonjs,binary:static_codegen/ --grpc_out=static_codegen/ --plugin=protoc-gen-grpc=`which grpc_tools_node_protoc_plugin` proto/Student.proto
  ```

- grpcServer2.js

  ```javascript
  var service = require('../static_codegen/proto/Student_grpc_pb');
  var message = require('../static_codegen/proto/Student_pb');
  
  var grpc = require('grpc');
  
  var server = new grpc.Server();
  
  server.addService(service.StudentServiceService, {
      getRealNameByUserName: getRealNameByUserName,
      getStudentByAge: getStudentByAge,
      getStudentWrapperByAges: getStudentWrapperByAges,
      biTalk: biTalk
  });
  
  server.bind('127.0.0.1:8899', grpc.ServerCredentials.createInsecure());
  server.start();
  
  function getRealNameByUserName(call, callback) {
      console.log('username：' + call.request.getUsername());
  
      var myResponse = new message.MyResponse();
      myResponse.setRealname('赵六');
  
      callback(null, myResponse);
  }
  
  function getStudentByAge() {}
  
  function getStudentWrapperByAges() {}
  
  function biTalk() {}
  ```

- grpcClient2.js

  ```java
  var service = require('../static_codegen/proto/Student_grpc_pb');
  var message = require('../static_codegen/proto/Student_pb');
  
  var grpc = require('grpc');
  
  var client = new service.StudentServiceClient('127.0.0.1:8899', grpc.credentials.createInsecure());
  
  var request =  new message.MyRequest();
  request.setUsername('王五');
  
  
  client.getRealNameByUserName(request, function (error, respData) {
      console.log(respData.getRealname());
  })
  ```


# Java I/O系统

## 课程目标

- 理解Java I/O系统

- 熟练使用java.io包中的相关类与接口进行I/O编程

- 掌握Java I/O的设计原则与使用的设计模式


## Java I/O

- 对于程序设计者来说，设计一个令人满意的I/O（输入输出）系统，是件艰巨的任务

  摘自《Thinking in Java》

### 流的概念

- Java程序通过流来完成输入/输出。流是生产或消费信息的抽象。流通过Java的输入/输出系统与物理设备链接。尽管与它们链接的物理设备不尽相同，所有流的行为具有同样的方式。这样，相同的输入/输出类和方法适用于所有类型的外部设备。这意味着一个输入流能够抽象多种不同类型的输入；从磁盘文件，从键盘或从网络套接字。同样，一个输出流可以输出到控制台，磁盘文件或相连的网络。流是处理输入和输出的一个洁净的方法，例如它不需要代码理解键盘和网络的不同。Java中流的实现是在java.io包定义的类层次结构内部的。

### 输入/输出流概念

- 输入/输出时，数据在通信通道中流动。所谓"数据流（stream）"指的是所有数据通信通道之中，数据的起点和终点。信息的通道就是一个数据流。只要是数据从一个地方"流"到另外一个地方，这种数据流动的通道都可以成为数据流。

- 输入/输出是相对于程序来说的。程序在使用数据时所扮演的角色有两个：一个是源，一个是目的。若程序是数据流的源，即数据的提供者，这个数据流对程序来说就是一个"输出数据流"（数据从程序流出）。若程序是数据流的终点，这个数据流对程序而言就是一个"输入数据流"（数据从程序外流向程序）

### 输入/输出类

- 在java.io包中提供了60多个类（流）
- 从功能上分为两大类：输入流和输出流
- 从流结构上可分为字节流（以字节为处理单位或面向字节）和字符流（以字符为处理单位或称为面向字符）
- 字节流的输入流和输出流基础是InputStream和OutputStream这两个抽象类，字节流的输入输出操作由这两个类的子类实现。字符流是Java1.1版后新增加的以字符为单位进行输入输出处理的流，字符流输入输出的基础是抽象类Reader和Writer

### 字节流和字符流

- Java 2定义了两种类型的流：字节流和字符流。字节流（byte stream）为处理字节的输入和输出提供了方便的方法。例如使用字节流读取或写入二进制数据。字符流（character stream）为字符的输入和输出提供了方便。它们采用了统一的编码标准，因而可以国际化。当然，在某些场合，字符流比字节流更有效

- Java的原始版本（Java 1.0）不包括字符流，因此所有的输入和输出都是以字节为单位的。Java 1.1中加入了字符流功能

- 需要声明：在最底层、所有的输入/输出都是字节形式的。基于字符的流只为处理字符提供方便有效的方法

  | byte stream  | character stream |
  | ------------ | ---------------- |
  | InputStream  | Reader           |
  | OutputStream | Writer           |

### 流的分类

- 两种基本的流是：输入流（Input Stream）和输出流（Output Stream）。可从中读出一系列字节的对象称为输入流。而能向其中写入一系列字节的对象称为输出流

- 读数据的逻辑为：

  open a stream

  while more information

  read information

  close the stream

- 写数据的逻辑为：

  open a stream

  while more information

  write information

  close the stream

- 节点流：从特定的地方读写的流类，例如：磁盘或一块内存区域

- 过滤流：使用节点流作为输入或输出。过滤流是使用一个已经存在的输入流或输出流连接创建的。

![QQ20191024-215617](/Users/agan/Pictures/QQ20191024-215617.png)



- java.io包中InputStream的类层次

![](http://journals.ecs.soton.ac.uk/java/tutorial/java/io/images/inputstreams_trans.gif)

### InputStream

- InputStream中包含一套字节输入流需要的方法，可以完成最基本的从输入流读入数据的功能。当Java程序需要外设的数据时，可根据数据的不同形式，创建一个适当的InputStream子类型的对象来完成与该外设的连接，然后再调用执行这个流类对象的特定输入方法来实现对相应外设的输入操作。
- InputStream类子类对象自然也继承了InputStream类的方法。常用的方法有：读数据的方法read()，获取输入流字节数的方法available()，定位输入位置指针的方法skip()，reset()，mark()等。

### OutputStream

- 三个基本的写方法

  abstract void write(int b)：往输出流中写入一个字节。

  abstract void write(int[] b)：往输出流中写入数组b中的所有字节。

  abstract void write(int[] b, int off, int len)：往输出流中写入数组b中从偏移量off开始的len个字节的数据 。

- 其他方法

  void flush()：刷新输出流，强制缓冲区中的输出字节被写出。

  void close()：关闭输出流，释放和这个流相关的系统资源。

- OutputStream是定义了流式字节输出模式的抽象类。该类的所有方法返回一个void值并且在出错情况下引发一个IOException异常。
- 通过打开字节数据
- java.io包中OutputStream的类层次

![](http://journals.ecs.soton.ac.uk/java/tutorial/java/io/images/outputstreams_trans.gif)

### 过滤流

- 在InputStream类和OutputStream类子类中，FilterInputStream和FilterOutputStream过滤流抽象类又派生出DataInputStream和DataOutputStream数据输入输出流类等子类。

### I/O流的链接

- Input Stream Chain

  ![](/Users/agan/Pictures/QQ20191024-233750.png)

- Output Stream Chain

![](/Users/agan/Pictures/QQ20191024-235014.png)

- java.io包中Reader的类层次

- java.io包中Writer的类层次

  ![](http://www.c-jump.com/bcc/c257c/Week10/const_images/IO_InputOutputReadersWriters.png)

### Java I/O库的设计原则

- Java的I/O库提供了一个称做链接的机制，可以将一个流与另一个流首尾相接，形成一个流管道的链接。这种机制实际上是一种被称为Decorator（装饰）设计模式的应用。

- 通过流的链接，可以动态的增加流的功能，而这种功能的增加是通过组合一些流的基本功能而动态获取的。

- 我们要获取一个I/O对象，往往需要产生多个I/O对象，这也是Java I/O库不太容易掌握的原因，但在I/O库中的

  Decorator模式的运用，给我们提供了实现上的灵活性。

###装饰模式（Decorator）

- 装饰模式又名包装（Wrapper）模式。
- 装饰模式以对客户端透明的方式扩展对象的功能，是继承关系的一个替代方案。
- 装饰模式以对客户端透明的方式动态的给一个对象附加更多的责任。换言之，客户端并不会觉得对象在装饰前和装饰后有什么不同。
- 装饰模式可以在不创造更多子类的情况下，将对象的功能加以扩展。
- 装饰模式把客户端的调用委派到被装饰类。装饰模式的关键在于这种扩展完全是透明的。
- 装饰模式是在不必改变原类文件和使用继承的情况下，动态的扩展一个对象的功能。它是通过创建一个包装对象，也就是装饰来包裹真实的对象。
- 装饰模式的角色
  - 抽象构件角色（Component）：给出一个抽象接口，以规范准备接收附加责任的对象。
  - 具体构件角色（Concrete Component）：定义一个将要接受附加责任的类
  - 装饰角色（Decorator）：持有一个构件（Component）对象的引用，并定义一个与抽象构建接口一致的接口
  - 具体装饰角色（Concrete Decorator）：负责给构件对象"贴上"附加的责任
- 装饰模式的特点
  - 装饰对象和真实对象有相同的接口。这样客户端对象就可以以和真实对象相同的方式和装饰对象交互。
  - 装饰对象包含一个真实对象的引用（reference）
  - 装饰对象接收所有来自客户端的请求。它把这些请求转发给真实的对象。
  - 装饰对象可以在转发这些请求以前或以后增加一些附加功能。这样就确保了在运行时，不用修改给的对象的结构就可以在外部增加附加的功能。在面向对象的设计中，通常是通过继承来实现对给定类的功能扩展。

### 装饰模式 VS 继承

- 装饰模式
  - 用来扩展特定对象的功能
  - 不需要子类
  - 动态
  - 运行时分配职责
  - 防止由于子类而导致的复杂和混乱
  - 更多的灵活性
  - 对于一个给定的对象，同时可能有不同的装饰对象，客户端可以通过它的需要选择合适的装饰对象发送消息。
- 继承
  - 用来扩展一类对象的功能
  - 需要子类
  - 静态
  - 编译时分派职责
  - 导致很多子类产生
  - 缺乏灵活性

###装饰模式（Decorator）的适用性

- 想要透明并且动态地给对象增加新的职责（方法）而又不会影响其他对象
- 给对象增加的职责在未来可能会发生改变
- 用子类扩展功能不实际的情况下

### java.io 与 java.nio

- java.io中最为核心的一个概念是流（stream），面向流的编程。Java中，一个流要么是输入流，要么是输出流，不可能同时既是输入流又是输出流。
- java.nio中拥有3个核心概念：Selector，Channel与Buffer。在java.nio中，我们是面向块（block）或是缓冲区（buffer）编程的。Buffer本身就是一块内存，底层实现上，它实际上是个数组。数据的读、写都是通过Buffer来实现的。
- 除了数组之外，Buffer还提供了对于数据的结构化访问方式，并且可以追踪到系统的读写过程。
- Java中的7种原生数据类型都有各自对应的Buffer类型，如IntBuffer，LongBuffer、ByteBuffer及CharBuffer等等，并没有BooleanBuffer类型。
- Channel指的是可以向其写入数据或是从中读取数据的对象，它类似java.io中的Stream。
- 所有数据的读写都是通过Buffer来进行的，永远不会出现直接向Channel写入数据的情况，或是直接从Channel读取数据的情况。
- 与Stream不同的是，Channel是双向的，一个流只可能是InputStream或是OutputStream，Channel打开后则可以进行读取、写入或是读写。
- 由于Channel是双向的，因此它能更好地反映出底层操作系统的真实情况；在Linux系统中，底层操作系统的通道就是双向的。

![](http://tutorials.jenkov.com/images/java-nio/overview-selectors.png)

![](http://tutorials.jenkov.com/images/java-nio/overview-channels-buffers.png)

- 关于NIO Buffer中3个重要状态属性的含义：position，limit与capacity。

  0 <= mark <= position <= limit <= capacity

  ![](http://tutorials.jenkov.com/images/java-nio/buffers-modes.png)

- 通过NIO读取文件涉及到三个步骤
  1. 从FileInputStream获取到FileChannel对象
  2. 创建Buffer
  3. 将数据从Channel获取到Buffer中

- 绝对方法与相对方法的含义
  1. 相对方法：limit值与position值会在操作时被考虑到
  2. 绝对方法：完全忽略limit与position值

### Socket编程 BIO

![](https://static.javatpoint.com/core/images/socket-programming.png)

- Server

```java
public class ReverseServer {
 
    public static void main(String[] args) {
        if (args.length < 1) return;
 
        int port = Integer.parseInt(args[0]);
 
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server is listening on port " + port);
 
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
 
                new ServerThread(socket).start();
            }
 
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

class ServerThread extends Thread {
    private Socket socket;
 
    public ServerThread(Socket socket) {
        this.socket = socket;
    }
 
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
 
 
            String text;
 
            do {
                text = reader.readLine();
                String reverseText = new StringBuilder(text).reverse().toString();
                writer.println("Server: " + reverseText);
 
            } while (!text.equals("bye"));
 
            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
```

- Client

```java
public class ReverseClient {
 
    public static void main(String[] args) {
        if (args.length < 2) return;
 
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
 
        try (Socket socket = new Socket(hostname, port)) {
 
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
 
            Console console = System.console();
            String text;
 
            do {
                text = console.readLine("Enter text: ");
 
                writer.println(text);
 
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
                String time = reader.readLine();
 
                System.out.println(time);
 
            } while (!text.equals("bye"));
 
            socket.close();
 
        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
```

### 编码

- ASCII（American Standard Code for Information Interchange，美国信息交换标准代码）

​	7 bit来表示一个字符，共计可以表示128种字符。

- ISO-8859-1

​	8 bit表示一个字符，即用一个字节（byte）（8 bit）来表示一个字符，共计可以表示256个字符。

- gb2312

  两个字节表示一个汉字。

- gbk

  可以表示繁体字。

- gb18030

- big5

  台湾 繁体字

- unicode

  采用了两个字节表示一个字符。

- UTF， Unicode Translation Format

  Unicode是一种编码方式，而UTF则是一种存储方式；UTF-8是Unicode的实现方式之一。

- UTF-16LE（little endian），UTF16-BE（big endian）

  Zero Width No-Break Space，0xFEFF（BE），0xFFFE（LE）

- UTF-8，变长字节表示形式

  一般来说，UTF-8会通过3个字节来表示一个中文

- BOM，Byte Order Mark

  即上面的0xFEFF，0xFFFE

### NIO零拷贝

- BIO

![](https://xunnanxu.github.io/2016/09/10/It-s-all-about-buffers-zero-copy-mmap-and-Java-NIO/non_zero_copy.png)

- NIO

![](https://xunnanxu.github.io/2016/09/10/It-s-all-about-buffers-zero-copy-mmap-and-Java-NIO/zero_copy.png)

- scatter-n-gather

![](https://xunnanxu.github.io/2016/09/10/It-s-all-about-buffers-zero-copy-mmap-and-Java-NIO/scattergather.png)

- mmap

  ![](https://xunnanxu.github.io/2016/09/10/It-s-all-about-buffers-zero-copy-mmap-and-Java-NIO/mmap.png)

##Reactor模式：反应器模式

Proactor模式

Netty整体架构是Reactor模式的完整体现

###Reactor模式的角色构成

（Reactor模式一共有5种角色构成）：

1. Handle（句柄或是描述符）：本质上表示一种资源，是由操作系统提供的；该资源用于表示一个个的事件，比如说文件描述符，或是针对网络编程中的Socket描述符。事件即可以来自外部，也可以来自于内部；外部事件比如客户端的连接请求，客户端发送过来数据等；内部事件比如操作系统产生的定时器事件等。它本质上就是一个文件描述符。Handle是事件产生的发源地。
2. Synchronous Event Demultiplexer（同步事件分离器）：它本身是一个系统调用，用于等待事件的发生（事件可能是一个，也可能是多个）。调用方在调用它的时候会被阻塞，一直阻塞到同步事件分离器上有事件产生为止。对于Linux来说，同步事件分离器指的就是常用的I/O多路复用机制，比如说select、poll、epoll等。在Java NIO领域中，同步事件分离器对应的组件就是Selector；对应的阻塞方法就是select方法。
3. Event Handler（事件处理器）：本身由多个回调方法构成，这些回调方法构成了与应用相关的对于某个事件的反馈机制。Netty相比于Java NIO来说，在事件处理器这个角色上进行了一个升级，它为我们开发者提供了大量的回调方法，供我们在特定事件产生时实现相应的回调方法进行业务逻辑的处理。
4. Concrete Event Handler（具体事件处理器）：是事件处理器的实现。它本身实现了事件处理器所提供的各个回调方法，从而实现了特定于业务的逻辑。它本质上就是我们所编写的一个个处理器实现。
5. Initiation Dispatcher（初始分发器）：实际上就是Reactor角色。它本身定义了一些规范，这些规范用于控制事件的调度方式，同时又提供了应用进行事件处理器的注册、删除等设施。它本身是整个事件处理器的核心所在，Initiation Dispatcher会通过事件同步分离器来等待事件的发生。一旦事件发生，Initiation Dispatcher首先会分离出每一个事件，然后调用事件处理器，最后调用相关的回调方法来处理这些事件。

### Reactor模式的流程

1. 当应用向Initiation Dispatcher注册具体的事件处理器时，应用会标识出该事件处理器希望Initiation Dispatcher在某个事件发生时向其通知该事件，该事件与Handle关联。
2. Initiation Dispatcher会要求每个事件处理器向其传递内部的Handle。该Handle向操作系统标识了事件处理器。
3. 当所有的事件处理器注册完毕后，应用会调用handle_events方法来启动Initiation Dispatcher的事件循环。这时，Initiation Dispatcher会将每个注册的事件管理器的Handle合并起来，并使用同步事件分离器等待这些事件的发生。比如说，TCP协议层会使用select同步事件分离器操作来等待客户端发送的数据到达连接的socket handle上。
4. 当与某个事件源对应的Handle变为ready状态时（比如说，TCP socket变为等待读状态时），同步事件分离器就会通知Initiation Dispatcher。
5. Initiation Dispatcher会触发事件处理器的回调方法，从而响应这个处于ready状态的Handle。当事件发生时，Initiation Dispatcher会被事件源激活的Handle作为key来寻找并分发恰当的事件处理器回调方法。
6. Initiation Dispatcher会回调事件处理器的handle_events回调方法来执行特定于应用的功能（开发者自己编写的功能），从而响应这个事件。所发生的事件类型可以作为该方法参数并被该方法内部使用来执行额外的特定于服务的分离与分发。

###源码总结

1. 一个EventLoopGroup当中会包含一个或多个EventLoop
2. 一个EventLoop在它的整个生命周期当中都只会与唯一一个Thread进行绑定
3. 所有由EventLoop所处理的各种I/O事件都将在它所关联的那个Thread上进行处理
4. 一个Channel在它的整个生命周期中只会注册在一个EventLoop上
5. 一个EventLoop在运行过程当中，会被分配给一个或者多个Channel

重要结论：在Netty中，Channel的实现一定是线程安全的；基于此，我们可以存储一个Channel的引用，并且在需要向远程端点发送数据时，通过这个引用来调用Channel相应的方法；即便当时有很多线程都在使用它也不会出现多线程问题；而且，消息一定会按照顺序发送出去

重要结论：我们在业务开发中，不要将长时间执行的耗时任务放入到EventLoop的执行队列中，因为它将会一直阻塞该线程所对应的所有Channel上的其他执行任务，如果我们需要进行阻塞调用或者耗时的操作（实际开发中很常见），那么我们就需要使用一个专门的EventExecutor（业务线程池）

通常会有两种实现方式：

1. 在ChannelHandler的回调方法中，使用自己定义的业务线程池，这样就可以实现异步调用
2. 借助于Netty提供的向ChannelPipeline添加ChannelHandler时调用的addList方法来传递EventExecutor

说明：默认情况下（调用addList(handler)）,ChannelHandler中的回调方法都是由I/O线程所执行，如果调用了ChannelPipeline addLast(EventExecutorGroup group, ChannelHandler... handlers)方法，那么ChannelHandler中的回调方法就是由参数中的group线程组来执行的



JDK所提供的Future只能通过手工方式检查执行结果，而这个操作是会阻塞的；Netty则对ChannelFuture进行了增强，通过ChannelFutureListener以回调的方式来获取执行结果，去除了手工检查阻塞的操作；值得注意的是：ChannelFutureListener的operationComplete方法是由I/O线程执行的，因此要注意的是不要在这里执行耗时操作，否则需要通过另外的线程或线程池来执行



在Netty中有两种发送消息的方式，可以直接写到Channel中，也可以写到ChannelHandler所关联的那个ChannelHandleContext中。对于前一种方式来说，消息会从ChannelPipeline的末尾开始流动；对于后一种方式来说，消息将从ChannelPipleline中的下一个ChannelHandler开始流动。

结论：

1. ChannelHandlerContext与ChannelHandler之间的关联绑定关系是永远都不会发生改变的，因此对其进行缓存是没有任何问题的。
2. 对于与Channel的同名方法来说，ChannelHandlerContext的方法将会产生更短的事件流，所以我们应该在可能的情况下利用这个特性来提升应用性能。



```java
// netty Oio的实现
try {
    .. Oio synchronous operation
} catch(SocketTimeoutException ex) {
}
```



```java
// C -> S1 -> S2
@Override
public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Bootstrap bootstrap = ...;
    bootstrap.group(ctx.channel().eventLoop())
        	.channel(NioSocketChannel.class)
 			.handler(..);
    bootstrap.connect()...
}
```



使用NIO进行文件读取所涉及的步骤

1. 从FileInputStream对象获取到Channel对象

2. 创建Buffer

3. 将数据从Channel中读取到Buffer对象中

   0 <= mark <= position <= limit <= capacity

Buffer

flip()方法

1. 将limit值设为当前的position
2. 将position设为0

clear()方法

1. 将limit值设为capacity
2. 将position值设为0

compact()方法

1. 将所有未读数据复制到Buffer起始位置处
2. 将position设为最后一个未读元素的后面
3. 将limit设为capacity
4. 现在Buffer就准备好了，但是不会覆盖未读的数据



ByteBuf 

注意：通过索引来访问ByteBuf时，并不会改变真实的读索引和写索引；我们可以通过ByteBuf的readIndex()和writeIndex()方法分别直接修改读索引和写索引。



Netty ByteBuf所提供的3种缓存区类型

1. heap buffer

2. direct buffer

3. composite buffer


Heap Buffer（堆缓存区）

这是最常用的类型，ByteBuf将数据存储到 JVM的堆空间中，并且将实际的数据存到byte array中来实现

优点：由于数据是存储在 JVM的堆中，因此可以快速的创建和快速的释放，并且它提供了直接访问内部字节数组的方法

缺点：每次读写数据时，都需要先将数据复制到直接缓存区中再进行网络传输



Direct Buffer（直接缓存区）

在堆之外直接分配内存空间，直接缓冲区并不会占用堆的容量空间，因为它是由操作系统在本地内存进行数据分配

优点：在使用Socket进行数据传递时，性能非常好，因为数据直接位于操作系统的本地内存中，所以不需要从 JVM将数据复制到直接缓存区中，性能很好

缺点：因为Direct Buffer是直接在操作系统内存中的，所以内存空间的分配和释放要比堆空间更加复杂，而且速度要慢一些

Netty通过提供内存池来解决这个问题。直接缓冲区并不支持通过字节数组的方式来访问数据

重点：对于后端的业务消息的编解码来说，推荐使用HeapByteBuf；对于I/O通信线程在读写缓冲区时，推荐使用DirectByteBuf



Composite Buffer（复合缓存区）

JDK的ByteBuffer与Netty的ByteBuf之间的差异比对：

1. Netty的ByteBuf采用了读写索引分离的策略（readerIndex与writerIndex），一个初始化（里面尚未有任何数据）的ByteBuf的readerIndex与writerIndex值都为0。
2. 当读索引与写索引处于同一个位置时，如果我们继续读取，那么就会抛出IndexOutOfBoundsException
3. 对于ByteBuf的任何读写操作都会分别单独维护读索引和写索引。maxCapacity最大容量默认的限制就是Integer.MAX_VALUE



JDK的ByteBuffer的缺点

1. final byte[] hb; 这是JDK的ByteBuffer对象中用于存储数据的对象声明；可以看到，其字节数组被声明为final的，也就是长度是固定不变的。一旦分配好后不能动态扩容和收缩；而且当待存储的数据字节很大时就很有可能出现IndexOutOfBoundsException。如果要预防这个异常，那就需要在存储之前完全确定好待存储的字节大小。如果ByteBuffer的空间不足，我们只有一种解决方案；创建一个全新的ByteBuffer中的数据复制过去，这一切操作都需要由开发者自己来手动完成。
2. ByteBuffer只使用一个position指针来标识位置信息，在进行读写切换时就需要调用flip方法或是rewind方法，使用起来很不方便。

Netty的ByteBuf的优点

1. 存储字节的数组是动态的，其最大值默认是Integer.MAX_VALUE。这里的动态性是体现在write方法中的，write方法在执行时会判断buffer容量，如果不足则自动扩容
2. ByteBuf的读写索引是完全分开的，使用起来就很方便



自旋锁

AtomicIntegerFieldUpdater要点总结

1. 更新器更新的必须是int类型变量，不能是其包装类型
2. 更新器更新的必须是volatile类型变量，确保线程之间共享变量时的立即可见性
3. 变量不能是static的，必须要是实例变量。因为sun.misc.Unsafe.objectFieldOffset()方法不支持静态变量（CAS操作本质上是通过对象实例的偏移量来直接进行赋值）

4. 更新器只能修改它可见范围内的变量，因为更新器是通过反射来得到这个变量，如果变量不可见就会报错

如果要更新的变量是包装类型，那么可以使用AtomicReferenceFieldUpdater来进行更新



Netty处理器重要概念

1. Netty的处理器可以分为两类：入站处理器与出站处理器
2. 入站处理器的顶层为ChannelInboundHandler，出站处理器的顶层是ChannelOutboundHandler
3. 数据处理时常用的各种编解码器本质上都是处理器
4. 编解码器：无论我们向网络中写入的数据是什么类型（int，char，String，二进制等），数据在网络中传递时，其都是以字节流的形式呈现的；将数据由原本的形式转换为字节流的操作称为编码（encode）,将数据由字节转换为它原本的格式或是其他格式的操作称为解码（decode），编解码统一称为codec
5. 编码：本质上是一种出站处理器；因此，编码一定是一种ChannelInboundHandler
6. 解码：本质上是一种入站处理器；因此，解码一定是一种ChannelOutboundHandler
7. 在Netty中，编码器通常以XXXEncoder命名；解码器通常以XXXDecoder命名



TCP粘包和拆包



关于Netty编解码器的重要结论

1. 无论是编码器还是解码器，其所接收的消息类型必须要与待处理的参数类型一致，否则该解码器或编码器并不会执行
2. 在解码器进行数据解码时，一定要记得判断缓冲（ByteBuf）中的数据是否足够，否则将会产生一些问题