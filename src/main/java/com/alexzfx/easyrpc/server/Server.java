package com.alexzfx.easyrpc.server;

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
 * Description : 服务端，收到请求后对方法进行本地调用，返回数据
 */
@Slf4j
public class Server {

    private final int bossEventLoopGroupSize = 1;
    private final int workerEventLoopGroupSize = 4;
    private final int defaultPort = 8890;

    private int port;

    public Server() {
        this.port = defaultPort;
    }

    public Server(int port) {
        this.port = port;
    }

    private void start() {
        EventLoopGroup bossEventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(bossEventLoopGroupSize) : new NioEventLoopGroup(bossEventLoopGroupSize);
        EventLoopGroup workerEventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(workerEventLoopGroupSize) : new NioEventLoopGroup(workerEventLoopGroupSize);
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
