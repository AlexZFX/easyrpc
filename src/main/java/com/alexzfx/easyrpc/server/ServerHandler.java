package com.alexzfx.easyrpc.server;

import com.alexzfx.easyrpc.protocol.entity.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Author : Alex
 * Date : 2018/10/2 19:32
 * Description :
 */
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {

    }
}
