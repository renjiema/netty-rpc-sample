package com.mrj.message;

/**
 * RpcResponseMessage
 *
 * @author by mrj
 * @date 2021/6/13 20:58
 * @description
 */
public class RpcResponseMessage extends Message {
    @Override
    protected int getMessageType() {
        return MessageTypeEnum.RPC_RESPONSE_MESSAGE_TYPE.getMessageType();
    }
}
