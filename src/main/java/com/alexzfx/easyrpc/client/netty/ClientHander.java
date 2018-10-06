package com.alexzfx.easyrpc.client.netty;

import com.alexzfx.easyrpc.client.future.FutureHolder;
import com.alexzfx.easyrpc.client.future.RpcFuture;
import com.alexzfx.easyrpc.protocol.entity.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Author : Alex
 * Date : 2018/10/2 19:35
 * Description :
 */
@Slf4j
@ChannelHandler.Sharable
public class ClientHander extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        log.info("receive a response  response id: " + msg.getRequestId());
        RpcFuture rpcFuture = FutureHolder.getAndRemoveFuture(msg.getRequestId());
        if (rpcFuture != null) {
            rpcFuture.setSuccess(msg);
        }
    }
}
