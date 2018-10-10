package com.alexzfx.easyrpc.server.netty;

import com.alexzfx.easyrpc.protocol.entity.RpcRequest;
import com.alexzfx.easyrpc.protocol.entity.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author : Alex
 * Date : 2018/10/2 19:32
 * Description :
 */
@Slf4j
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    public static ConcurrentHashMap<String, Object> clazzMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        log.info("recieve a request id : " + msg.getRequestId());
        RpcResponse rpcResponse = getResponse(msg);
        ctx.writeAndFlush(rpcResponse).addListener((GenericFutureListener<ChannelFuture>) future -> {
            if (!future.isSuccess()) {
                log.error(future.cause().getLocalizedMessage());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            if (cause.getLocalizedMessage().equals("远程主机强迫关闭了一个现有的连接。")) {
                log.info("一个客户端连接断开。");
                return;
            }
        }
        super.exceptionCaught(ctx, cause);
    }

    private RpcResponse getResponse(RpcRequest rpcRequest) {
        RpcResponse rpcResponse = new RpcResponse(rpcRequest.getRequestId());
        try {
            Class<?> clazz = Class.forName(rpcRequest.getClassName());

            Object c = clazzMap.get(rpcRequest.getClassName());
            if (c == null) {
                clazzMap.put(rpcRequest.getClassName(), clazz.newInstance());
                c = clazzMap.get(rpcRequest.getClassName());
            }
            String methodName = rpcRequest.getServiceName();
            Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
            Object[] parameters = rpcRequest.getParameters();
            FastClass fastClass = FastClass.create(clazz);
            FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);
            //与Spring联合使用时应该调用ApplicationContext里面的已有的bean
            Object result = fastMethod.invoke(c, parameters);

            rpcResponse.setResult(result);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            rpcResponse.setException(e);
        }
        return rpcResponse;
    }
}
