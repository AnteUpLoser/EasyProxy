package server.handler;

import static common.Constant.*;
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
        log.info("收到消息:{}",msg);
        byte type =msg.getType();
        switch (type){
            case ProxyMsg.TYPE_HEARTBEAT:

                break;
            case ProxyMsg.TYPE_CONNECT:
                String vid = new String(msg.getData());
                registerMainChannel(clientKey, ctx.channel());
                log.info("主通道绑定成功:{}",vid);
                break;
            case ProxyMsg.TYPE_TRANSFER:

            default:
                //其他异常操作
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("新连接:{}",ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    private static void registerMainChannel(String clientKey, Channel channel){
        Optional.ofNullable(pc.remove(clientKey)).ifPresent(ChannelOutboundInvoker::close);
        pc.put(clientKey,channel);
    }
}
