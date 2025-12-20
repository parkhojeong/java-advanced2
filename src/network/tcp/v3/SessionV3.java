package network.tcp.v3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static util.MyLogger.log;

public class SessionV3 implements Runnable{
    private final Socket socket;

    public SessionV3(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String receivedMessage = input.readUTF();
                log("[client -> server] received:" + receivedMessage);

                if (receivedMessage.equals("exit")) {
                    break;
                }

                String toSend = receivedMessage + "world";
                output.writeUTF(toSend);
                log("[client <- server] sent:" + toSend);
            }

            log("server close" + socket);
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
