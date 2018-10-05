package com.alexzfx.easyrpc.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * Author : Alex
 * Date : 2018/10/2 19:11
 * Description : 服务端，收到请求后对方法进行本地调用，返回数据，服务的注册要在此之前或者之后。
 */
@Slf4j
public class Server {

    private static final int DEFAULT_BossEventLoopGroupSize = 1;
    private static final int DEFAULT_WorkerEventLoopGroupSize = 4;

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {

        EventLoopGroup bossEventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(DEFAULT_BossEventLoopGroupSize) : new NioEventLoopGroup(DEFAULT_BossEventLoopGroupSize);
        EventLoopGroup workerEventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(DEFAULT_WorkerEventLoopGroupSize) : new NioEventLoopGroup(DEFAULT_WorkerEventLoopGroupSize);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childHandler(new ServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
        ;
        log.info("server start at port :" + port);
        try {
            // 绑定端口，接受连接，等待服务器Socket关闭
            ChannelFuture future = serverBootstrap.bind(port).sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();
            log.info("server stop");
        }

    }

}
