package server.container;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import protocol.ProxyMsgDecoder;
import protocol.ProxyMsgEncoder;
import server.common.Constant;
import server.handler.ClientHandler;

import java.net.InetSocketAddress;

@Slf4j
public class ServerSocket {
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public static void start(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new ProxyMsgDecoder())
                                .addLast(new ProxyMsgEncoder())
                                //连接保活机制
                                //.addLast(new IdleStateHandler(40, 10, 0))
                                //处理连接它的客户端
                                .addLast(new ClientHandler());
                    }
                }).bind(Constant.serverPort);

        channelFuture.addListener((ChannelFutureListener) future -> {
            if(future.isSuccess()) {
                InetSocketAddress address = (InetSocketAddress) future.channel().localAddress();
                log.info("Netty服务器已启动~ 地址: {}", address);
            }else {
                log.error("Netty服务器启动失败！");
            }
        });
    }
}
