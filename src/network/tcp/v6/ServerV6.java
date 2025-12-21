package network.tcp.v6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static util.MyLogger.log;

public class ServerV6 {
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        log("server start");
        SessionManagerV6 sessionManager = new SessionManagerV6();
        ServerSocket serverSocket = new ServerSocket(PORT);
        log("server socket:" + serverSocket);

        ShutdownHook shutdownHook = new ShutdownHook(serverSocket, sessionManager);
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook, "shutdown-hook"));
        try{
            while (true) {
                Socket socket = serverSocket.accept(); // blocking
                log("socket:" + socket);

                SessionV6 session = new SessionV6(socket, sessionManager);
                Thread thread = new Thread(session);
                thread.start();
            }
        } catch (IOException e) {
            log("server shutdown" + e);
        }
    }

    static class ShutdownHook implements Runnable {

        private final ServerSocket serverSocket;
        private final SessionManagerV6 sessionManager;

        public ShutdownHook(ServerSocket serverSocket, SessionManagerV6 sessionManager) {
            this.serverSocket = serverSocket;
            this.sessionManager = sessionManager;
        }

        @Override
        public void run() {
            log("server shutdown start");
            try {
                sessionManager.closeAll();
                serverSocket.close();

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("e = " + e);
            }
            log("server shutdown end");
        }
    }
}
