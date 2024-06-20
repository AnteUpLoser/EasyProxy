package server.common;

import java.nio.channels.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyMainChannelManager {
    private static final Map<String, Channel> mainChannelMap = new ConcurrentHashMap<>();


}
