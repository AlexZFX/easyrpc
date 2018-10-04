package com.alexzfx.easyrpc.client;

import io.netty.util.concurrent.FastThreadLocal;

import java.util.HashMap;

/**
 * Author : Alex
 * Date : 2018/10/3 20:37
 * Description :
 */
public class FutureHolder {

    private static FastThreadLocal<HashMap<Long, RpcFuture>> futureHolder = new FastThreadLocal<>();

    public static void registerFuture(long requestId, RpcFuture future) {
        if (futureHolder.get() == null) {
            futureHolder.set(new HashMap<>());
        }
        futureHolder.get().put(requestId, future);
    }

    public static RpcFuture getAndRemoveFuture(long requestId) {
        return futureHolder.get().remove(requestId);
    }


}
