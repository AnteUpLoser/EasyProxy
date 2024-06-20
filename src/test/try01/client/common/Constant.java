package client.common;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constant {
    /**
     * 代理服务channel
     */
    public static Channel proxyChannel = null;

    /**
     * 绑定访客id
     */
    public static final AttributeKey<String> VID = AttributeKey.newInstance("vid");

    /**
     * 访客，代理服务channel
     */
    public static Map<String, Channel> vpc = new ConcurrentHashMap<>();

    /**
     * 访客，真实服务channel
     */
    public static Map<String, Channel> vrc = new ConcurrentHashMap<>();

    /**
     * 真实服务端口
     */
    public static int realPort = 9090;

    /**
     * Netty代理服务器端口
     */
    public static int serverPort = 8080;

    /**
     * Netty服务器ip
     */
    public static String serverIp = "127.0.0.1";
}
