package com.alexzfx.easyrpc.commom.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author : Alex
 * Date : 2018/10/2 20:05
 * Description : 注解于实现的接口类上，表示该类是用于使用远程 rpc服务的 class，其中的method都会通过动态代理调用到远程的服务端
 */
//注解的声明周期为始终不会丢弃
@Retention(RetentionPolicy.RUNTIME)
//注解的使用地点为 类，接口或enum声明
@Target(ElementType.TYPE)
public @interface RpcInterface {
}
