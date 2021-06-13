package com.mrj.message;

/**
 * RpcRequestMessage
 *
 * @author by mrj
 * @date 2021/6/13 20:57
 * @description
 */
public class RpcRequestMessage extends Message {
    @Override
    protected int getMessageType() {
        return MessageTypeEnum.RPC_REQUEST_MESSAGE_TYPE.getMessageType();
    }
}
