package server.common;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Constant {
    /** 客户端服务channel */
    public static Channel clientChannel = null;

    /** 绑定channel_id */
    public static final AttributeKey<String> VID = AttributeKey.newInstance("vid");

    /** 访客，客户服务channel */
    public static final Map<String, Channel> vcc = new ConcurrentHashMap<>();

    /** 访客，访客服务channel */
    public static final  Map<String, Channel> vvc = new ConcurrentHashMap<>();

    /** 访客访问端口 */
    public static int visitorPort = 5000;

    /** Netty服务器端口 */
    public static int serverPort = 8080;

    public static void registerMainChannel(String uid, Channel channel){
        Optional.ofNullable( vvc.remove(uid)).ifPresent(old->old.close());
        vvc.put(uid,channel);
    }

    public static Channel getMainChannel(String uid){
        return vvc.get(uid);
    }
}
