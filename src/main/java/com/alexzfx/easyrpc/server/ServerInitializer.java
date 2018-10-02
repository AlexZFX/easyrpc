package com.alexzfx.easyrpc.server;

import com.alexzfx.easyrpc.protocol.codec.RpcRequestDecoder;
import com.alexzfx.easyrpc.protocol.codec.RpcResponseEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Author : Alex
 * Date : 2018/10/2 19:33
 * Description :
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new ServerHandler())
                .addLast(new RpcRequestDecoder())
                .addLast(new RpcResponseEncoder());
    }
}
