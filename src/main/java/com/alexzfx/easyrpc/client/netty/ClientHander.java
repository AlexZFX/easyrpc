package com.alexzfx.easyrpc.client.netty;

import com.alexzfx.easyrpc.client.future.FutureHolder;
import com.alexzfx.easyrpc.client.future.RpcFuture;
import com.alexzfx.easyrpc.protocol.entity.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Author : Alex
 * Date : 2018/10/2 19:35
 * Description :
 */
@ChannelHandler.Sharable
public class ClientHander extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        RpcFuture rpcFuture = FutureHolder.getAndRemoveFuture(msg.getRequestId());
        rpcFuture.trySuccess(msg);
    }
}
