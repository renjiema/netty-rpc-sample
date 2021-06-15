package com.mrj.handler;

import com.mrj.message.RpcResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RpcResponseMessageHandler
 *
 * @author by mrj
 * @date 2021/6/15 14:57
 * @description
 */
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {
    public static final Map<Integer, Promise<Object>> PROMISES = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponseMessage message) throws Exception {
        final Promise<Object> promise = PROMISES.remove(message.getSequenceId());
        if (promise != null) {
            if (message.getExceptionValue() != null) {
                promise.setFailure(message.getExceptionValue());
            } else {
                promise.setSuccess(message.getReturnValue());
            }
        }
    }
}
