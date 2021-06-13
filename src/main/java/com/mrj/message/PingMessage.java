package com.mrj.message;

/**
 * PingMessage
 *
 * @author by mrj
 * @description
 */
public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return MessageTypeEnum.PING_MESSAGE_TYPE.getMessageType();
    }
}
