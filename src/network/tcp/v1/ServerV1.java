package network.tcp.v1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static util.MyLogger.log;

public class ServerV1 {
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        log("server start");
        ServerSocket serverSocket = new ServerSocket(PORT);
        log("server socket:" + serverSocket);

        Socket socket = serverSocket.accept();
        log("socket:" + socket);

        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        String receivedMessage = input.readUTF();
        log("[client -> server] received:" + receivedMessage);

        String toSend = receivedMessage + "world";
        output.writeUTF(toSend);
        log("[client <- server] sent:" + toSend);

        log("server close" + socket);
        input.close();
        output.close();
        socket.close();
        serverSocket.close();
    }
}
