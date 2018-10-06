package com.alexzfx.easyrpc.server.netty;

import com.alexzfx.easyrpc.protocol.codec.RpcDecoder;
import com.alexzfx.easyrpc.protocol.codec.RpcEncoder;
import com.alexzfx.easyrpc.protocol.entity.RpcRequest;
import com.alexzfx.easyrpc.protocol.entity.RpcResponse;
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
                .addLast(new RpcDecoder(RpcRequest.class))
                .addLast(new RpcEncoder(RpcResponse.class))
                .addLast(new ServerHandler());
    }
}
