package com.alexzfx.easyrpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/10/6 15:58
 * Description :
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> targerClass;

    public RpcDecoder(Class<?> targerClass) {
        this.targerClass = targerClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLen = in.readInt();
        if (dataLen < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLen) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLen];
        in.readBytes(data);
        Object obj = ProtoStuffUtil.deserializer(data, targerClass);
        out.add(obj);
    }
}
