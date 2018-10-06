package com.alexzfx.easyrpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Author : Alex
 * Date : 2018/10/6 15:54
 * Description :
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> targetClazz;

    public RpcEncoder(Class<?> targetClazz) {
        this.targetClazz = targetClazz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (targetClazz.isInstance(msg)) {
            byte[] data = ProtoStuffUtil.serializer(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
