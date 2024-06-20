package client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import protocol.ProxyMsg;

import static common.Constant.*;

@Slf4j
public class ProxySocketHandler extends SimpleChannelInboundHandler<ProxyMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMsg msg) throws Exception {
        byte type = msg.getType();
        switch (type) {
            case ProxyMsg.TYPE_TRANSFER:
                ByteBuf buf = ctx.alloc().buffer(msg.getData().length);
                buf.writeBytes(msg.getData());
                rsc.get(clientKey).writeAndFlush(buf);
                log.info("访客数据已转发给内网服务");

        }

    }
}
