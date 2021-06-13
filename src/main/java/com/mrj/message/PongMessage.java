package com.mrj.message;

/**
 * PongMessage
 *
 * @author by mrj
 * @date 2021/6/13 20:51
 * @description
 */
public class PongMessage extends Message {

    @Override
    protected int getMessageType() {
        return MessageTypeEnum.PONG_MESSAGE_TYPE.getMessageType();
    }
}
