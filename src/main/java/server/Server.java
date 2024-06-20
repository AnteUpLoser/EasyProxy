package server;

import client.Client;
import server.container.ServerSocket;
import server.container.VisitorSocket;

public class Server {
    public static void main(String[] args) {
        ServerSocket.start();
        VisitorSocket.start();
    }
}
