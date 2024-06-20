package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Documented;

import static protocol.Constant.BODY_LEN;
@Slf4j
public class ProxyMsgDecoder extends LengthFieldBasedFrameDecoder {
    public ProxyMsgDecoder(){
        super(Integer.MAX_VALUE, 0, 4, 0, 0);
    }
    @Override
    protected ProxyMsg decode(ChannelHandlerContext ctx, ByteBuf in2) throws Exception {
        ByteBuf in = (ByteBuf) super.decode(ctx, in2);
        log.info("in:{}",in);
        if(in==null||in.readableBytes()<4){
            return null;
        }
        int dataLen = in.readInt();
        byte type = in.readByte();
        ProxyMsg msg = new ProxyMsg();
        msg.setType(type);
        if(dataLen>1){
            byte[] data = new byte[dataLen - 1];
            in.readBytes(data);
            msg.setData(data);
        }
        log.info("msg:{}",msg);
        in.release();
        return msg;
    }
}
