package client.container;

import client.handler.ProxyHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import protocol.ProxyMsg;
import protocol.ProxyMsgDecoder;
import protocol.ProxyMsgEncoder;

import java.nio.charset.StandardCharsets;

import static client.common.Constant.*;

@Slf4j
public class ProxySocket {
    private static final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    public static void start(String vid){
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup).channel(NioSocketChannel.class
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new ProxyMsgDecoder())
                                .addLast(new ProxyMsgEncoder())
                                .addLast(new ProxyHandler());
                    }
                });

        bootstrap.connect(serverIp, serverPort).addListener((ChannelFutureListener) channelFuture -> {
            if(channelFuture.isSuccess()){
                log.info("代理客户端成功启动连接到代理服务端");
                Channel channel = channelFuture.channel();
                if(StringUtil.isNullOrEmpty(vid)){
                    //vid传空是 client的连接
                    ProxyMsg msg = new ProxyMsg();
                    msg.setType(ProxyMsg.TYPE_CONNECT);
                    msg.setData("src/test/try01/client".getBytes());
                    channel.writeAndFlush(msg);

                    //设置代理通道是为当前新建的这条
                    proxyChannel = channel;
                    log.info("已经发送client");
                }else{
                    ProxyMsg msg = new ProxyMsg();
                    msg.setType(ProxyMsg.TYPE_CONNECT);
                    msg.setData(vid.getBytes());
                    channel.writeAndFlush(msg).addListener((ChannelFutureListener) channelFuture1 -> {
                        if(channelFuture1.isSuccess()){
                            log.info("已经发送vid");
                            vpc.put(vid, channel);

                        }
                    });
                }
            }else {
                log.error("代理客户端连接代理服务端失败！");
            }
        });
    }

}
