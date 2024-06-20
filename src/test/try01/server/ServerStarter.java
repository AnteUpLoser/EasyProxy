package server;

import client.container.ServiceSocket;
import server.container.ServerSocket;
import server.container.VisitorSocket;

public class ServerStarter {
    public static void main(String[] args) {
        ServerSocket.start();
        VisitorSocket.start();
    }
}
