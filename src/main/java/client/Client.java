package client;

import client.container.ProxySocket;
import client.container.ServiceSocket;

public class Client {
    public static void main(String[] args) {
        ProxySocket.start();
        ServiceSocket.start();
    }
}
