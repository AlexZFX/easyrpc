package com.alexzfx.easyrpc.protocol.codec;

import com.alexzfx.easyrpc.protocol.entity.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Author : Alex
 * Date : 2018/10/2 19:18
 * Description :
 */
public class RpcRequestEncoder extends MessageToByteEncoder<RpcRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcRequest rpcRequest, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(ProtoStuffUtil.serializer(rpcRequest));
    }
}
