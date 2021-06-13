package com.mrj.message;

/**
 * RpcRequestMessage
 *
 * @author by mrj
 * @description
 */
public class RpcRequestMessage extends Message {
    @Override
    public int getMessageType() {
        return MessageTypeEnum.RPC_REQUEST_MESSAGE_TYPE.getMessageType();
    }
}
