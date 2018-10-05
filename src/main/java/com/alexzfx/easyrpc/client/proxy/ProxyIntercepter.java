package com.alexzfx.easyrpc.client.proxy;

import com.alexzfx.easyrpc.client.ClientServer;
import com.alexzfx.easyrpc.client.future.FutureHolder;
import com.alexzfx.easyrpc.client.future.RpcFuture;
import com.alexzfx.easyrpc.client.netty.Client;
import com.alexzfx.easyrpc.protocol.entity.RpcRequest;
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
        if (client != null) {
            RpcFuture rpcFuture = new RpcFuture();
            FutureHolder.registerFuture(rpcRequest.getRequestId(), rpcFuture);
            client.getChannel().writeAndFlush(rpcRequest);
            return rpcFuture.get();
        } else {
            log.error("no rpcService is available :" + rpcRequest.getClassName());
            return null;
        }
    }
}
