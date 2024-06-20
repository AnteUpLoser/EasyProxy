package server.container;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import server.handler.VisitorHandler;

import java.net.InetSocketAddress;

import static server.common.Constant.visitorPort;

@Slf4j
public class VisitorSocket {
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();


    public static void start(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture future = serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                //处理连接它的访客(客户端)
                                .addLastnew VisitorHandler());
                    }
                }).bind(visitorPort);

        future.addListener((ChannelFutureListener) future1 -> {
            if(future1.isSuccess()){
                log.info("访客到访 ····················");
            }else {
                log.error("访客请求连接失败");
            }
        });

    }
}
