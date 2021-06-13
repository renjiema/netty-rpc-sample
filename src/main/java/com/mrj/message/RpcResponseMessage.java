package com.mrj.message;

/**
 * RpcResponseMessage
 *
 * @author by mrj
 * @description
 */
public class RpcResponseMessage extends Message {
    @Override
    public int getMessageType() {
        return MessageTypeEnum.RPC_RESPONSE_MESSAGE_TYPE.getMessageType();
    }
}
