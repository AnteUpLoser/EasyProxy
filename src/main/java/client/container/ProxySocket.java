package client.container;

import client.handler.ProxySocketHandler;
import common.Constant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.ProxyHandler;
import lombok.extern.slf4j.Slf4j;
import protocol.ProxyMsg;
import protocol.ProxyMsgDecoder;
import protocol.ProxyMsgEncoder;

import static common.Constant.*;

@Slf4j
public class ProxySocket {
    private static final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    //建立用户代理主通道
    public static void start(){
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new ProxyMsgDecoder())
                                .addLast(new ProxyMsgEncoder())
                                .addLast(new ProxySocketHandler());
                    }
                });

        bootstrap.connect(serverIp, serverPort).addListener((ChannelFutureListener) channelFuture -> {
            if(channelFuture.isSuccess()){
                Channel channel = channelFuture.channel();
                log.info("代理客户端成功与代理服务器连接");
                ProxyMsg msg = new ProxyMsg();
                msg.setType(ProxyMsg.TYPE_CONNECT);
                msg.setData(clientKey.getBytes());
                channel.writeAndFlush(msg).addListener((ChannelFutureListener) channelFuture1 -> {
                    if(channelFuture1.isSuccess()){
                        log.info("已经发送建立主通道请求");
                        pc.put(clientKey, channel);
                    }
                });
            }

        });
    }
}
