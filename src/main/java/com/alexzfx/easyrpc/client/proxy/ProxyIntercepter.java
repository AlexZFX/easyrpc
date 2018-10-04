package com.alexzfx.easyrpc.client.proxy;

import com.alexzfx.easyrpc.protocol.entity.RpcRequest;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Author : Alex
 * Date : 2018/10/3 19:07
 * Description :
 */
public class ProxyIntercepter implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] parameters, MethodProxy methodProxy) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setServiceName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameters(parameters);
        return null;
    }
}
