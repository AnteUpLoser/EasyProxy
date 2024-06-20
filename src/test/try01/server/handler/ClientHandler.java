package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import protocol.ProxyMsg;
import server.common.Constant;

import static server.common.Constant.VID;
import static server.common.Constant.vvc;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<ProxyMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMsg msg) throws Exception {

        Channel clientChannel = ctx.channel();
        byte type =msg.getType();
        switch (type){
            case ProxyMsg.TYPE_HEARTBEAT:
                ProxyMsg heartbeatMsg = new ProxyMsg();
                heartbeatMsg.setType(ProxyMsg.TYPE_HEARTBEAT);
                clientChannel.writeAndFlush(heartbeatMsg);
                break;
            case ProxyMsg.TYPE_CONNECT:
                String vid = new String(msg.getData());
                Constant.registerMainChannel(vid,ctx.channel());
                log.info("主通道绑定成功:{}",vid);
                break;
            case ProxyMsg.TYPE_TRANSFER:
                // 把数据转到用户服务
                ByteBuf buf = ctx.alloc().buffer(msg.getData().length);
                buf.writeBytes(msg.getData());

                String visitorId = ctx.channel().attr(Constant.VID).get();
                Channel vChannel = Constant.vvc.get(visitorId);
                if (null != vChannel) {
                    vChannel.writeAndFlush(buf);
                }
                break;
            default:
                //其他异常操作
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("新连接:{}",ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

}
