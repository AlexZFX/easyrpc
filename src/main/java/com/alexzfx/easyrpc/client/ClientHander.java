package com.alexzfx.easyrpc.client;

import com.alexzfx.easyrpc.protocol.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Author : Alex
 * Date : 2018/10/2 19:35
 * Description :
 */
public class ClientHander extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {

    }
}
