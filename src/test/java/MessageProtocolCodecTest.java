import com.mrj.message.Message;
import com.mrj.message.PingMessage;
import com.mrj.protocol.MessageProtocolCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static com.mrj.protocol.MessageProtocolCodec.MAGIC_NUMBER;

/**
 * MessageProtocolCodecTest
 *
 * @author by mrj
 * @description
 */
public class MessageProtocolCodecTest {

    @Test
    public void testEncode() {
        final EmbeddedChannel channel = new EmbeddedChannel(
                new MessageProtocolCodec());
        final PingMessage message = new PingMessage();
        final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        final MessageProtocolCodec codec = new MessageProtocolCodec();
        channel.writeOutbound(message);

    }

    @Test
    public void testDecode() throws IOException {
        final EmbeddedChannel channel = new EmbeddedChannel(
                new MessageProtocolCodec());
        final PingMessage message = new PingMessage();
        final ByteBuf buf = messageToByteBuf(message);
        channel.writeInbound(buf);
    }

    public static ByteBuf messageToByteBuf(Message msg) throws IOException {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(MAGIC_NUMBER);
        out.writeByte(1);
        out.writeByte(1);
        out.writeByte(msg.getMessageType());
        out.writeInt(msg.getSequenceId());
        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(msg);
            bytes = bos.toByteArray();
        }
        out.writeInt(bytes.length);
        out.writeByte(0xff);
        out.writeBytes(bytes);
        return out;
    }
}
