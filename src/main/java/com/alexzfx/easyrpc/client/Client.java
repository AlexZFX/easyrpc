package com.alexzfx.easyrpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * Author : Alex
 * Date : 2018/10/2 19:34
 * Description : 客户端，代理通过该端发送请求，考虑保持长连接，打算一个client与一个地址绑定
 */
@Slf4j
public class Client {

    private EventLoopGroup eventLoopGroup;

    private Channel channel;

    private ChannelFuture channelFuture;

    private String host;

    private int port;

    public Client(String host, int port) {
        this(host, port, Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1));
    }

    public Client(String host, int port, EventLoopGroup eventLoopGroup) {
        this.host = host;
        this.port = port;
        this.eventLoopGroup = eventLoopGroup;
    }

    public ChannelFuture connectChannel() {
        if (channelFuture == null) {
            channelFuture = new Bootstrap().group(eventLoopGroup)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                    .handler(new ClientHander())
                    .connect(host, port)
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            channel = future.channel();
                            log.info("start a client at " + host + ":" + port);
                            channel.closeFuture().addListener((ChannelFutureListener) closefuture -> {
                                log.info("stop a client at " + host + ":" + port);
                            });
                        } else {
                            log.error("start a Client failed", future.cause());
                        }
                    })
            ;
        }
        return channelFuture;
    }

    public Channel getChannel() {
        if (channel != null) {
            return channel;
        } else {
            channelFuture = connectChannel();
            return channel;
        }
    }

}