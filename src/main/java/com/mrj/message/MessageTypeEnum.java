package com.mrj.message;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * MessageTypeEnum
 *
 * @author by mrj
 * @description
 */
@Slf4j
@Getter
public enum MessageTypeEnum {
    /** 萍消息类型 */
    PING_MESSAGE_TYPE(1, PingMessage.class),
    /** pong消息类型 */
    PONG_MESSAGE_TYPE(2, PongMessage.class),
    /** rpc请求消息类型 */
    RPC_REQUEST_MESSAGE_TYPE(3, RpcRequestMessage.class),
    /** rpc响应消息类型 */
    RPC_RESPONSE_MESSAGE_TYPE(4, RpcResponseMessage.class);


    private final int messageType;

    private final Class<? extends Message> messageClass;

    MessageTypeEnum(int messageType, Class<? extends Message> messageClass) {
        this.messageType = messageType;
        this.messageClass = messageClass;
    }

    public static Class<? extends Message> getMessageClass(int messageType) {
        for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()) {
            if (messageTypeEnum.getMessageType() == messageType) {
                return messageTypeEnum.getMessageClass();
            }
        }
        log.error("Failed parse message type:{}", messageType);
        throw new RuntimeException("Failed parse message type");
    }

}
