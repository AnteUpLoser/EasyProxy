package common;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constant {
    /** 用户唯一表示 ClientKey */
    public static final String clientKey = "12345";

    /** 代理服务器ip */
    public static final String serverIp = "127.0.0.1";
    /** 代理服务器端口 */
    public static final int serverPort = 8080;

    /** 访客访问的端口 */
    public static final int visitPort = 5000;

    /** 真实服务的IP地址 以及端口 */
    public static final String realServiceIp = "127.0.0.1";
    public static final int realServicePort = 9090;

    /** 用户代理主通道 netty客户端==服务端 proxyChannel key: clientKey  value: channel */
    public static Map<String, Channel> pc = new ConcurrentHashMap<>();
    /** 用户代理的路由通道 route channel 用于传输数据 key: clientKey  value: channel */
    public static Map<String, Channel> rc = new ConcurrentHashMap<>();
    /** 用户的真实服务通道 realServiceChannel  key: clientKey    value: channel */
    public static Map<String, Channel> rsc = new ConcurrentHashMap<>();
    /** 访客的通道 */
    public static Map<String, Channel> vc = new ConcurrentHashMap<>();



}
