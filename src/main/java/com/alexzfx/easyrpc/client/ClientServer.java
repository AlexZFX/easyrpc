package com.alexzfx.easyrpc.client;

import com.alexzfx.easyrpc.client.netty.Client;
import com.alexzfx.easyrpc.commom.annotation.RpcInterface;
import com.alexzfx.easyrpc.commom.etcd.EndPoint;
import com.alexzfx.easyrpc.commom.etcd.EtcdRegistry;
import com.alexzfx.easyrpc.commom.etcd.IRegistry;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author : Alex
 * Date : 2018/10/5 19:35
 * Description :
 */
@Slf4j
public class ClientServer {

    private IRegistry registry;

    private static ConcurrentHashMap<String, Client> clientMap = new ConcurrentHashMap<>();

    private final String packagePath;

    public ClientServer(String packagePath) {
        this.packagePath = packagePath;
        this.registry = new EtcdRegistry();
//        this.clientMap = new ConcurrentHashMap<>();
    }

    public void start() {
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RpcInterface.class);
        EventLoopGroup eventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(4) : new NioEventLoopGroup(4);
        classes.forEach(clazz -> {
            try {
                List<EndPoint> endPoints = registry.find(clazz.getName());
                //没做负载均衡，只使用了找到的第一个服务
                if (!endPoints.isEmpty()) {
                    clientMap.put(clazz.getName(), new Client(endPoints.get(0).getHost(), endPoints.get(0).getPort(), eventLoopGroup));
                }
            } catch (Exception e) {
                log.error("registry find error", e);
            }
        });
    }

    public static ConcurrentHashMap<String, Client> getClientMap() {
        return clientMap;
    }
}
