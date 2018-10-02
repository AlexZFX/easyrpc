package com.alexzfx.easyrpc.protocol.codec;

import com.alexzfx.easyrpc.protocol.entity.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Author : Alex
 * Date : 2018/10/2 19:18
 * Description :
 */
public class RpcResponseEncoder extends MessageToByteEncoder<RpcResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResponse rpcResponse, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(ProtoStuffUtil.serializer(rpcResponse));
    }
}
