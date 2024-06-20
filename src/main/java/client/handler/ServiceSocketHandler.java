package client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import protocol.ProxyMsg;

import static common.Constant.*;

@Slf4j
public class ServiceSocketHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //读取到真实服务数据
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        ProxyMsg myMsg = new ProxyMsg();
        myMsg.setType(ProxyMsg.TYPE_TRANSFER);
        myMsg.setData(bytes);

        Channel proxyChannel = pc.get(clientKey);
        if (null != proxyChannel) {
            proxyChannel.writeAndFlush(myMsg);
            log.info("内网服务handler： 已转发给代理通道");
        }
    }
}
