package com.alexzfx.easyrpc.protocol.entity;

import lombok.Data;

/**
 * Author : Alex
 * Date : 2018/10/2 15:25
 * Description :
 */
@Data
public class RpcRequest {
    // 请求Id
    private long requestId;
    // 类名
    private String className;
    // 服务名
    private String serviceName;
    // 参数类型
    private Class<?>[] parameterTypes;
    // 参数
    private Object[] parameters;
}
