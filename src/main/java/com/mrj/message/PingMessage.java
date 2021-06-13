package com.mrj.message;

/**
 * PingMessage
 *
 * @author by mrj
 * @date 2021/6/13 20:48
 * @description
 */
public class PingMessage extends Message {
    @Override
    protected int getMessageType() {
        return MessageTypeEnum.PING_MESSAGE_TYPE.getMessageType();
    }
}
