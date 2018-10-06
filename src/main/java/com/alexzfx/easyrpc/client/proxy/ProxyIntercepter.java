package com.alexzfx.easyrpc.client.proxy;

import com.alexzfx.easyrpc.client.ClientServer;
import com.alexzfx.easyrpc.client.future.FutureHolder;
import com.alexzfx.easyrpc.client.future.RpcFuture;
import com.alexzfx.easyrpc.client.netty.Client;
import com.alexzfx.easyrpc.protocol.entity.RpcRequest;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Author : Alex
 * Date : 2018/10/3 19:07
 * Description :
 */
@Slf4j
public class ProxyIntercepter implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] parameters, MethodProxy methodProxy) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setServiceName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameters(parameters);
        Client client = ClientServer.getClientMap().get(rpcRequest.getClassName());
        RpcFuture rpcFuture;
        if (client != null) {
            ChannelFuture channelFuture = client.connectChannel();
            rpcFuture = new RpcFuture(channelFuture.channel().eventLoop());
            if (channelFuture.isSuccess()) {
                sendRequest(rpcRequest, rpcFuture, channelFuture);
            } else {
                channelFuture.addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        sendRequest(rpcRequest, rpcFuture, future);
                    } else {
                        log.error("send request error ", future.cause());
                    }
                });
            }
            return rpcFuture.get().getResult();
        } else {
            log.error("no rpcService is available :" + rpcRequest.getClassName());
            return null;
        }
    }

    private void sendRequest(RpcRequest rpcRequest, RpcFuture rpcFuture, ChannelFuture channelFuture) {
        channelFuture.channel().writeAndFlush(rpcRequest)
                .addListener((ChannelFutureListener) writefuture -> {
                    if (writefuture.isSuccess()) {
                        FutureHolder.registerFuture(rpcRequest.getRequestId(), rpcFuture);
                        log.info("send request success");
                    } else {
                        rpcFuture.tryFailure(writefuture.cause());
                        log.error("send request failed", writefuture.cause());
                    }
                });
    }
}
