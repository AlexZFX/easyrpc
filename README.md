## EasyRPC

[项目完整介绍博客](http://www.alexzfx.com/2018/10/11/%E5%AE%9E%E7%8E%B0%E4%B8%80%E4%B8%AA%E7%AE%80%E5%8D%95%E7%9A%84RPC%E6%A1%86%E6%9E%B6/)

```
git clone https://github.com/AlexZFX/easyrpc
```
Open the package and install the project into local repository 
```
mvn install
```
Add dependency
```
<dependency>
    <groupId>com.alexzfx</groupId>
    <artifactId>easyrpc</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

**Open your local [etcd](https://github.com/etcd-io/etcd) client at port 2379.    
If you don't have, you can download it [here](https://github.com/etcd-io/etcd/releases)**


Define service interface

```
package com.alexzfx.easyrpc.common;

public interface HelloService {
    String sayHello();
}

```

Prepare a server service
```
package com.alexzfx.easyrpc.server;

import com.alexzfx.easyrpc.commom.annotation.RpcService;
import com.alexzfx.easyrpc.common.HelloService;

@RpcService
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello() {
        return "Hello World";
    }
}

```

Start a service server
```
package com.alexzfx.easyrpc.server;

public class ServerApplication {

    public static void main(String[] args) {
        ServerMain serverMain = new ServerMain("com.alexzfx.easyrpc.server");
        serverMain.start();
    }
}


```



Prepare a client impl
```
package com.alexzfx.easyrpc.client;

import com.alexzfx.easyrpc.commom.annotation.RpcInterface;
import com.alexzfx.easyrpc.common.HelloService;

@RpcInterface
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello() {
        return null;
    }
}

```

Start a client server  

```
package com.alexzfx.easyrpc.client;

import com.alexzfx.easyrpc.client.proxy.ProxyFactory;
import com.alexzfx.easyrpc.common.HelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ClientApplication {

    public ClientApplication() {
        ClientServer clientServer = new ClientServer("com.alexzfx.easyrpc.client");
        clientServer.start();
        helloService = ProxyFactory.create(HelloServiceImpl.class);
    }

    private HelloService helloService;

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class);
    }

    @RequestMapping("/")
    public String sayhello() {
        return helloService.sayHello();
    }
}

```

Access the website _http:://localhost:8080_  
Then you will see the `Hello World`;





