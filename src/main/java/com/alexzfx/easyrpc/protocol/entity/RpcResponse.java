package com.alexzfx.easyrpc.protocol.entity;

import lombok.Data;

/**
 * Author : Alex
 * Date : 2018/10/2 15:25
 * Description :
 */
@Data
public class RpcResponse {
    private long requestId;
    private Throwable exception;
    private Object result;
}
