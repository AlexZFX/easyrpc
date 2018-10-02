package com.alexzfx.easyrpc.protocol.codec;

import com.alexzfx.easyrpc.protocol.entity.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/10/2 19:18
 * Description :
 */
public class RpcResponseDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        Object o = ProtoStuffUtil.deserializer(byteBuf.array(), RpcResponse.class);
        list.add(o);
    }
}
