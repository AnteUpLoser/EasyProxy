package client.handler;

import client.common.ConnectUtil;
import client.common.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import protocol.ProxyMsg;

public class ProxyHandler extends SimpleChannelInboundHandler<ProxyMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMsg msg) throws Exception {
// 客户端读取到代理过来的数据了
        byte type = msg.getType();
        String vid = new String(msg.getData());
        switch (type) {
            //待做
            case ProxyMsg.TYPE_HEARTBEAT:
            case ProxyMsg.TYPE_DISCONNECT:
                break;
            case ProxyMsg.TYPE_CONNECT:
                ConnectUtil.connectRealServer(vid);
                break;
            case ProxyMsg.TYPE_TRANSFER:
                // 把数据转到真实服务
                ByteBuf buf = ctx.alloc().buffer(msg.getData().length);
                buf.writeBytes(msg.getData());

                String visitorId = ctx.channel().attr(Constant.VID).get();
                Channel rchannel = Constant.vrc.get(visitorId);
                if (null != rchannel) {
                    rchannel.writeAndFlush(buf);
                }
                break;
            default:
                // 操作有误
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        String vid = ctx.channel().attr(Constant.VID).get();
        if (StringUtil.isNullOrEmpty(vid)) {
            super.channelWritabilityChanged(ctx);
            return;
        }
        Channel realChannel = Constant.vrc.get(vid);
        if (realChannel != null) {
            realChannel.config().setOption(ChannelOption.AUTO_READ, ctx.channel().isWritable());
        }
    }
}
