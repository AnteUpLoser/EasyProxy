package client.common;

import client.container.ProxySocket;
import client.container.ServiceSocket;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;

public class ConnectUtil {

    //访客连接客户端
    public static void connectProxyServer(String vid) {
        ProxySocket.start(vid);
        /*if (StringUtil.isNullOrEmpty(vid)) {
            if (Constant.proxyChannel == null || !Constant.proxyChannel.isActive()) {
                ProxySocket.start(null);
            }
            return null;
        } else {
            Channel channel = Constant.vpc.get(vid);
            if (null == channel) {
                ProxySocket.start(null);
                channel = Constant.vpc.get(vid);
            }
            return channel;
        }*/
    }

    //客户端访客连接真实服务
    public static Channel connectRealServer(String vid) {
        if (StringUtil.isNullOrEmpty(vid)) {
            return null;
        }
        Channel channel = Constant.vrc.get(vid);
        if (null == channel) {
            ServiceSocket.start(vid);
            channel = Constant.vrc.get(vid);
        }
        return channel;
    }


}
