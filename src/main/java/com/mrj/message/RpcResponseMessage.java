package com.mrj.message;

import lombok.Getter;
import lombok.Setter;

/**
 * RpcResponseMessage
 *
 * @author by mrj
 * @description
 */
@Setter
@Getter
public class RpcResponseMessage extends Message {
    /**
     * 返回值
     */
    private Object returnValue;
    /**
     * 异常值
     */
    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return MessageTypeEnum.RPC_RESPONSE_MESSAGE_TYPE.getMessageType();
    }
}
