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

import java.net.InetSocketAddress;
import java.util.UUID;

import static server.common.Constant.*;

@Slf4j
public class VisitorHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress visitorAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        log.info("访客远程地址: {}", visitorAddress);

        // 访客连接上代理服务器Channel
        Channel visitorChannel = ctx.channel();
        // 先不读取访客数据
        visitorChannel.config().setOption(ChannelOption.AUTO_READ, false);

        // 生成访客ID
        String vid = UUID.randomUUID().toString();

        // 绑定访客通道
        visitorChannel.attr(VID).set(vid);
        vvc.put(vid, visitorChannel);

        ProxyMsg msg = new ProxyMsg();
        msg.setType(ProxyMsg.TYPE_CONNECT);
        msg.setData(vid.getBytes());
        clientChannel.writeAndFlush(msg);


        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        String vid = ctx.channel().attr(VID).get();
        if (StringUtil.isNullOrEmpty(vid)) {
            log.error("访客未绑定通道, visitorID不存在");
            return;
        }
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        ProxyMsg msg = new ProxyMsg();
        msg.setType(ProxyMsg.TYPE_TRANSFER);
        msg.setData(bytes);

        //发送给访客服务器的客户端 也就是Netty代理的服务端
        Channel clientChannel = vcc.get(vid);
        clientChannel.writeAndFlush(msg);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel visitorChannel = ctx.channel();
        String vid = visitorChannel.attr(Constant.VID).get();
        if (StringUtil.isNullOrEmpty(vid)) {
            super.channelWritabilityChanged(ctx);
            return;
        }
        Channel clientChannel = Constant.vcc.get(vid);

        //保持访客和客户端通道可写性一致
        if (clientChannel != null) {
            clientChannel.config().setOption(ChannelOption.AUTO_READ, visitorChannel.isWritable());
        }

        super.channelWritabilityChanged(ctx);
    }
}
