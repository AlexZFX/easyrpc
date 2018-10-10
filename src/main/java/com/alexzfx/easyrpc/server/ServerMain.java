package com.alexzfx.easyrpc.server;

import com.alexzfx.easyrpc.commom.annotation.RpcService;
import com.alexzfx.easyrpc.commom.etcd.EtcdRegistry;
import com.alexzfx.easyrpc.commom.etcd.IRegistry;
import com.alexzfx.easyrpc.server.netty.Server;
import com.alexzfx.easyrpc.server.netty.ServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Author : Alex
 * Date : 2018/10/5 19:47
 * Description :
 */
@Slf4j
public class ServerMain {

    private static final int DEFAULT_SERVER_PORT = 8890;

    private IRegistry registry;

    private int port;

    private final String packagePath;


    public ServerMain(String packagePath) {
        this(packagePath, new EtcdRegistry());
    }

    public ServerMain(String packagePath, IRegistry registry) {
        this.registry = registry;
        this.packagePath = packagePath;
        this.port = System.getProperty("server.port") == null ? DEFAULT_SERVER_PORT : Integer.parseInt(System.getProperty("server.port"));
    }

    public void start() {
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RpcService.class);
        classes.forEach(clazz -> {
            try {
                Class<?>[] interfaces = clazz.getInterfaces();
                String clazzName = clazz.getName();
                if (interfaces != null && interfaces.length > 0) {
                    clazzName = interfaces[0].getName();
                }
                //注册的是 接口名 和 服务实例
                ServerHandler.clazzMap.put(clazzName, clazz.newInstance());
                registry.register(clazzName, port);
            } catch (Exception e) {
                log.error("register service failed : " + e.getLocalizedMessage(), e);
            }
        });
//      //新开线程的话会程序会退出
//        new Thread(() -> {
        Server server = new Server(port);
        server.start();
//        }).start();
    }
}
