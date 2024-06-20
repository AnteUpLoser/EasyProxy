package client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.ProxyMsg;

import static common.Constant.*;

public class ServiceSocketHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //读取到真实服务数据
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        ProxyMsg myMsg = new ProxyMsg();
        myMsg.setType(ProxyMsg.TYPE_TRANSFER);
        myMsg.setData(bytes);

        Channel visitorChannel = vc.get(clientKey);
        if (null != visitorChannel) {
            visitorChannel.writeAndFlush(myMsg);
        }
    }
}
