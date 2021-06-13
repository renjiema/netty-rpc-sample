package com.mrj.protocol;

import com.mrj.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 解析自定义protocol，转换ByteBuf和Message
 *
 * @author by mrj
 */
@Slf4j
public class MessageProtocolCodec extends MessageToMessageCodec<ByteBuf, Message> {

    public static final byte[] MAGIC_NUMBER = {(byte) 0xce, (byte) 0xce, (byte) 0xba, (byte) 0xbe};
    private static final int MAGIC_NUMBER_INT = -825312578;
    public static final int PROTOCOL_VERSION = 1;

    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, List<Object> list) throws Exception {
        final ByteBuf buf = ctx.alloc().buffer();
        //4字节：魔数
        buf.writeBytes(MAGIC_NUMBER);
        //1字节：版本
        buf.writeByte(PROTOCOL_VERSION);
        //1字节：序列化方式
        buf.writeByte(1);
        //1字节：messageType
        buf.writeByte(msg.getMessageType());
        //4字节：序列化id
        buf.writeInt(msg.getSequenceId());
        //4字节：message长度
        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(msg);
            bytes = bos.toByteArray();
        }
        buf.writeBytes(bytes);
        //1字节：补位对齐16字节
        buf.writeByte(0x00);
        //内容
        buf.writeBytes(bytes);
        list.add(buf);
    }

    @Override
    public void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list) throws Exception {
        final int magicNum = buf.readInt();
        if (magicNum != MAGIC_NUMBER_INT) {
            log.error("Failed to verify magic number:{}", magicNum);
            throw new RuntimeException("Unsupported magic number");
        }
        final byte version = buf.readByte();
        final byte serializerType = buf.readByte();
        final byte messageType = buf.readByte();
        final int sequenceId = buf.readInt();
        final int length = buf.readInt();
        buf.readByte();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes, 0, length);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message message = (Message) ois.readObject();
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("{}", message);
        list.add(message);

    }
}
