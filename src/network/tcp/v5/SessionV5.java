package network.tcp.v5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static network.tcp.SocketCloseUtil.closeAll;
import static util.MyLogger.log;

public class SessionV5 implements Runnable {
    private final Socket socket;

    public SessionV5(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (socket;
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        ) {

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

        } catch (IOException e) {
            log(e);
        }

        log("socket " + socket + "closed: " + socket.isClosed());
    }
}
