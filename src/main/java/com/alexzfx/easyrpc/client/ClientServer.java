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
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author : Alex
 * Date : 2018/10/5 19:35
 * Description :
 */
@Slf4j
public class ClientServer {

    private IRegistry registry;

    //设置一个endpoint使用一个client，netty高效理论上满足使用
    private static ConcurrentHashMap<EndPoint, Client> clientMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, List<EndPoint>> serviceMap = new ConcurrentHashMap<>();

    private final String packagePath;

    private static final Random random = new Random();

    public ClientServer(String packagePath) {
        this.packagePath = packagePath;
        this.registry = new EtcdRegistry();
    }

    public void start() {
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RpcInterface.class);
        EventLoopGroup eventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(4) : new NioEventLoopGroup(4);
        //定时任务线程池，定时更新服务列表，设置为5分钟
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        classes.forEach(clazz -> executorService.scheduleAtFixedRate(() -> {
            try {
                //拿到当前仍在注册中心中的相应服务列表
                // TODO 删除掉对应失效的endpoint
                Class<?>[] interfaces = clazz.getInterfaces();
                String className = clazz.getName();
                if (interfaces != null && interfaces.length > 0) {
                    className = interfaces[0].getName();
                }
                List<EndPoint> list = registry.find(className);
                serviceMap.put(className, list);
                list.forEach(endPoint -> {
                    if (clientMap.get(endPoint) == null) {
                        Client client = new Client(endPoint.getHost(), endPoint.getPort(), eventLoopGroup);
                        clientMap.put(endPoint, client);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 3 * 60, TimeUnit.SECONDS));

    }

    public static Client getClient(String serviceName) {
        List<EndPoint> endPoints = serviceMap.get(serviceName);
        // 简单的负载均衡，只使用了随机选择
        if (endPoints != null) {
            EndPoint endPoint = endPoints.get(random.nextInt(endPoints.size()));
            return clientMap.get(endPoint);
        }
        return null;
    }
}
