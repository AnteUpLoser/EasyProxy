package client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.ProxyMsg;

import static common.Constant.*;

public class ProxySocketHandler extends SimpleChannelInboundHandler<ProxyMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMsg msg) throws Exception {
        byte type = msg.getType();
        Channel curChannel = ctx.channel();
        switch (type) {
            case ProxyMsg.TYPE_TRANSFER:
                // 把数据转到真实服务
                ByteBuf buf = ctx.alloc().buffer(msg.getData().length);
                buf.writeBytes(msg.getData());
                rsc.get(clientKey).writeAndFlush(buf);
        }

    }
}
