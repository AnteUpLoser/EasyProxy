package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import static protocol.Constant.DATA_LEN_SIZE;
import static protocol.Constant.TYPE_SIZE;

/**
 * 自定义编码器
 * 将ProxyMsg消息编码成 有格式的字节流
 * 消息出站时调用 <br>
 * 协议格式如下: <br>
 * | body_len (5 bytes) | type (1 byte) | data (variable length) |
 */
@Slf4j
public class ProxyMsgEncoder extends MessageToByteEncoder<ProxyMsg> {

    //提供空参构造
    public ProxyMsgEncoder(){}
    @Override
    protected void encode(ChannelHandlerContext ctx, ProxyMsg msg, ByteBuf out) throws Exception {
        log.info("msg encode:{}",msg);
        int bodyLen = TYPE_SIZE;

        if(msg.getData() != null){
            bodyLen += msg.getData().length;
        }
        //先写入消息体长度 单位字节
        out.writeInt(bodyLen);
        //写入消息体类型
        out.writeByte(msg.getType());
        //写入消息携带的data
        if (msg.getData() != null) {
            out.writeBytes(msg.getData());
        }
    }
}
