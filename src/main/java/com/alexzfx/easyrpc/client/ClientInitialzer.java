package com.alexzfx.easyrpc.client;

import com.alexzfx.easyrpc.protocol.codec.RpcRequestEncoder;
import com.alexzfx.easyrpc.protocol.codec.RpcResponseDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Author : Alex
 * Date : 2018/10/2 19:35
 * Description :
 */
public class ClientInitialzer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new ClientHander())
                .addLast(new RpcRequestEncoder())
                .addLast(new RpcResponseDecoder());
    }
}
