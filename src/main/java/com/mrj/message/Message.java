package com.mrj.message;

import lombok.Data;

import java.io.Serializable;

/**
 * Message
 *
 * @author by mrj
 * @description
 */
@Data
public abstract class Message implements Serializable {
    public static Class<? extends Message> getMessageClass(int messageType) {
        return MessageTypeEnum.getMessageClass(messageType);
    }

    private int sequenceId;

    private int messageType;

    /**
     * 得到消息类型
     *
     * @return int
     */
    protected abstract int getMessageType();

}
