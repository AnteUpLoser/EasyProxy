package client.container;

import client.common.ConnectUtil;
import client.common.Constant;
import client.handler.ServiceHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.StringUtil;

public class ServiceSocket {
    static EventLoopGroup eventLoopGroup = new NioEventLoopGroup();


    public static void start(String vid) {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ServiceHandler());
                        }

                    });
            bootstrap.connect("127.0.0.1", Constant.realPort).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        // 客户端链接真实服务成功
                        future.channel().config().setOption(ChannelOption.AUTO_READ, false);
                        future.channel().attr(Constant.VID).set(vid);
                        Constant.vrc.put(vid, future.channel());
                        ConnectUtil.connectProxyServer(vid);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
