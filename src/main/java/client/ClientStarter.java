package client;

import client.common.ConnectUtil;

public class ClientStarter {
    public static void main(String[] args) {
        ConnectUtil.connectProxyServer("123");
    }
}
