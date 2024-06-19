package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.lang.annotation.Documented;

import static protocol.Constant.BODY_LEN;


public class ProxyMsgDecoder extends LengthFieldBasedFrameDecoder {
    public ProxyMsgDecoder(){
        super(Integer.MAX_VALUE, 0, 4, 0, 0);
    }

    @Override
    protected ProxyMsg decode(ChannelHandlerContext ctx, ByteBuf in2) throws Exception {
        ByteBuf in = (ByteBuf) super.decode(ctx, in2);
        if(in == null || in.readableBytes() < BODY_LEN){
            return null;  //数据不够解码
        }

        int dataLen = in.readInt();
        byte type = in.readByte();
        byte[] data = new byte[dataLen - BODY_LEN];
        in.readBytes(data);
        ProxyMsg msg = new ProxyMsg();
        msg.setType(type);
        msg.setData(data);
        return msg;

    }
}
