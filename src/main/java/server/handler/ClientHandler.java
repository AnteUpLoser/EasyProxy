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
                // 绑定访客和客户端的连接
                Channel visitorChannel = Constant.vvc.get(vid);
                if (StringUtil.isNullOrEmpty(vid) || "client".equals(vid)) {
                    Constant.clientChannel = ctx.channel();
                }else {
                    if (null != visitorChannel) {
                        ctx.channel().attr(Constant.VID).set(vid);
                        Constant.vcc.put(vid, ctx.channel());

                        // 通道绑定完成可以读取访客数据 并触发访客处理器的channelWritabilityChanged操作和Read0操作
                        visitorChannel.config().setOption(ChannelOption.AUTO_READ, true);
                    }else {
                        log.error("代理服务端报错: 访客和客户端绑定连接失败！");
                    }
                }
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
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel clientChannel = ctx.channel();
        String vid = clientChannel.attr(VID).get();
        if(StringUtil.isNullOrEmpty(vid)) {
            super.channelWritabilityChanged(ctx);
            return;
        }
        Channel visitorChannel = vvc.get(vid);

        //保持访客和客户端通道可写性一致
        if (visitorChannel != null) {
            visitorChannel.config().setOption(ChannelOption.AUTO_READ, clientChannel.isWritable());
        }

        super.channelWritabilityChanged(ctx);
    }
}
