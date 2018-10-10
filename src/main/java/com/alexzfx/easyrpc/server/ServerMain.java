package com.alexzfx.easyrpc.server;

import com.alexzfx.easyrpc.commom.annotation.RpcService;
import com.alexzfx.easyrpc.commom.etcd.EtcdRegistry;
import com.alexzfx.easyrpc.commom.etcd.IRegistry;
import com.alexzfx.easyrpc.server.netty.Server;
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
                registry.register(clazz.getName(), port);
            } catch (Exception e) {
                log.error("register service failed : " + e.getLocalizedMessage(), e);
            }
        });
        new Thread(() -> {
            Server server = new Server(port);
            server.start();
        }).start();
    }
}
