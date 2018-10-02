package com.alexzfx.easyrpc.protocol.codec;

import com.alexzfx.easyrpc.protocol.entity.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/10/2 19:19
 * Description :
 */
public class RpcRequestDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        Object o = ProtoStuffUtil.deserializer(byteBuf.array(), RpcRequest.class);
        list.add(o);
    }
}
