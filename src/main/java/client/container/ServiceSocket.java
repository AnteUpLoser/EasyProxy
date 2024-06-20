package client.container;

import client.handler.ServiceSocketHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import static common.Constant.*;

@Slf4j
public class ServiceSocket {
    private static EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    public static void start() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ServiceSocketHandler());
                    }
                });
        bootstrap.connect(realServiceIp, realServicePort).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("内网服务端启动成功, 已绑定rsc");
                rsc.put(clientKey, future.channel());
            }else{
                log.error("内网服务端启动失败！");
            }
        });
    }
}
