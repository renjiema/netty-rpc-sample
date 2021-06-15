package com.mrj.handler;

import com.mrj.config.ServicesFactory;
import com.mrj.message.RpcRequestMessage;
import com.mrj.message.RpcResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RpcRequestMessageHandler
 *
 * @author by mrj
 * @description
 */
@Slf4j
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage message) throws Exception {
        RpcResponseMessage response = new RpcResponseMessage();
        response.setSequenceId(message.getSequenceId());
        Object invoke;
        try {
            Object service = ServicesFactory.getServiceImpl(message.getInterfaceName());
            Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterTypes());
            invoke = method.invoke(service, message.getParameterValue());
            response.setReturnValue(invoke);
        } catch (Exception e) {
            log.error("Rpc remote call error, request={}", message, e);
            final String msg = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            response.setExceptionValue(new Exception("Rpc remote call error:" + msg));
        }
        ctx.writeAndFlush(response);
    }

}
