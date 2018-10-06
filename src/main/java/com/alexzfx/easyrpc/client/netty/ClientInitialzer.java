package com.alexzfx.easyrpc.client.netty;

import com.alexzfx.easyrpc.protocol.codec.RpcDecoder;
import com.alexzfx.easyrpc.protocol.codec.RpcEncoder;
import com.alexzfx.easyrpc.protocol.entity.RpcRequest;
import com.alexzfx.easyrpc.protocol.entity.RpcResponse;
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
                .addLast(new RpcEncoder(RpcRequest.class))
                .addLast(new RpcDecoder(RpcResponse.class))
                .addLast(new ClientHander())
        ;
    }
}
