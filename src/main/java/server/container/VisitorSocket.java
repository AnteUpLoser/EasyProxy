package server.container;

import static common.Constant.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import server.handler.VisitorSocketHandler;

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
                                .addLast(new VisitorSocketHandler());
                    }
                }).bind(visitPort);

        future.addListener((ChannelFutureListener) future1 -> {
            if(future1.isSuccess()) {
                log.info("访客端启动成功");


            }else{
                log.info("访客端启动失败");
            }
        });

    }
}
