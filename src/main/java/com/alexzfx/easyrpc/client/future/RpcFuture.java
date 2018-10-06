package com.alexzfx.easyrpc.client.future;

import com.alexzfx.easyrpc.protocol.entity.RpcResponse;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;

/**
 * Author : Alex
 * Date : 2018/10/3 20:36
 * Description :
 */
public class RpcFuture extends DefaultPromise<RpcResponse> {
    // promise 要求必须有一个promise
    public RpcFuture(EventExecutor executor) {
        super(executor);
    }
}
