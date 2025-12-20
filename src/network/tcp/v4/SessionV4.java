package network.tcp.v4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static network.tcp.SocketCloseUtil.closeAll;
import static util.MyLogger.log;

public class SessionV4 implements Runnable{
    private final Socket socket;

    public SessionV4(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream input = null;
        DataOutputStream output = null;
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

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
        } finally {
            closeAll(socket, input, output);
            log("socket close: " + socket);
        }
    }
}
