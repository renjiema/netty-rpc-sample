package com.mrj.message;

/**
 * PongMessage
 *
 * @author by mrj
 * @description
 */
public class PongMessage extends Message {

    @Override
    public int getMessageType() {
        return MessageTypeEnum.PONG_MESSAGE_TYPE.getMessageType();
    }
}
