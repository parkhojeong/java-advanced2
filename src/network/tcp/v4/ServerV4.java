package network.tcp.v4;

import network.tcp.v4.SessionV4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static util.MyLogger.log;

public class ServerV4 {
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        log("server start");
        ServerSocket serverSocket = new ServerSocket(PORT);
        log("server socket:" + serverSocket);

        while (true) {
            Socket socket = serverSocket.accept(); // blocking
            log("socket:" + socket);

            SessionV4 session = new SessionV4(socket);
            Thread thread = new Thread(session);
            thread.start();
        }
    }
}
