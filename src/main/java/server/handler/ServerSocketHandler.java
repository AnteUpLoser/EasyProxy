package server.handler;

import static common.Constant.*;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import protocol.ProxyMsg;

import java.util.Optional;

@Slf4j
public class ServerSocketHandler extends SimpleChannelInboundHandler<ProxyMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMsg msg) throws Exception {

        byte type = msg.getType();
        Channel curChannel = ctx.channel();
        switch (type) {
            case ProxyMsg.TYPE_HEARTBEAT:

                break;
            case ProxyMsg.TYPE_CONNECT:
                String vid = new String(msg.getData());
                registerMainChannel(clientKey, ctx.channel());
                log.info("主通道绑定成功:{}", vid);
                break;
            case ProxyMsg.TYPE_TRANSFER:
                ByteBuf buf = ctx.alloc().buffer(msg.getData().length);
                buf.writeBytes(msg.getData());
                if(vc.get(clientKey) != null){
                    vc.get(clientKey).writeAndFlush(buf);
                    log.info("内网服务响应数据转发到netty服务端");
                    log.info("mag data{}",msg.getData());
                }


            default:
                //其他异常操作
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("新连接:{}", ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    private static void registerMainChannel(String clientKey, Channel channel) {
        Optional.ofNullable(pc.remove(clientKey)).ifPresent(ChannelOutboundInvoker::close);
        pc.put(clientKey, channel);
    }
}
