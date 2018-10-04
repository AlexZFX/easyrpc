package com.alexzfx.easyrpc.client.proxy;

import net.sf.cglib.proxy.Enhancer;

/**
 * Author : Alex
 * Date : 2018/10/3 19:05
 * Description :
 */
public class ProxyFactory {

    public static <T> T create(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new ProxyIntercepter());
        return (T) enhancer.create();
    }
}
