package com.alexzfx.easyrpc.protocol;

import com.alexzfx.easyrpc.protocol.codec.ProtoStuffUtil;
import com.alexzfx.easyrpc.protocol.entity.RpcResponse;
import org.junit.Test;

/**
 * Author : Alex
 * Date : 2018/10/2 16:30
 * Description :
 */
public class ProtoStuffUtilTest {

    @Test
    public void serializerTest() {
//        RpcRequest rpcRequest = new RpcRequest();
//        rpcRequest.setClassName("Good");
//        rpcRequest.setRequestId(1);
//        rpcRequest.setServiceName("hello");
//        rpcRequest.setParameterTypes(new Class[]{int.class, String.class});
//        rpcRequest.setParameters(new Object[]{1, "helloworld"});
//        System.out.println(new String(ProtoStuffUtil.serializer(rpcRequest)));
//        rpcRequest = ProtoStuffUtil.deserializer(ProtoStuffUtil.serializer(rpcRequest), RpcRequest.class);
//        System.out.println(rpcRequest);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setException(new RuntimeException("yes"));
        rpcResponse.setRequestId(1);
        rpcResponse.setResult(2);
        System.out.println(new String(ProtoStuffUtil.serializer(rpcResponse)));
        RpcResponse r2 = ProtoStuffUtil.deserializer(ProtoStuffUtil.serializer(rpcResponse), RpcResponse.class);
        r2.getException().printStackTrace();
    }
}