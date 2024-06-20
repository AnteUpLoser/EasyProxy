package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import protocol.ProxyMsg;

import static common.Constant.*;

@Slf4j
public class VisitorSocketHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        Channel visitorChannel = ctx.channel();
        Channel proxyChannel = pc.get(clientKey);
        if(proxyChannel == null){
            // 该端口还没有代理客户端
            log.error("proxyChannel is null ,visitorChannel being closed:{}",visitorChannel);
            ctx.channel().close();
        }else{
            //推送访客消息数据
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            ProxyMsg msg = new ProxyMsg();
            msg.setType(ProxyMsg.TYPE_TRANSFER);
            msg.setData(bytes);
            proxyChannel.writeAndFlush(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //建立访客通道传输数据
        vc.put(clientKey, ctx.channel());
        super.channelActive(ctx);
    }
}
