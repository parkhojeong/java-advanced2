package network.tcp.chat;

import network.tcp.SocketCloseUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static util.MyLogger.log;

public class ChatClient {
    public static void main(String[] args) throws InterruptedException {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        ) {

            log("소켓 연결:" + socket);
            Session session = new Session(socket, input, output);
            Thread sender = new Thread(new Sender(session));
            sender.start();

            while (true) {
                log("received : " + input.readUTF());
            }
        } catch (IOException e) {
            log(e);
        }
    }

    private static class Session {
        private final Socket socket;
        private final DataInputStream input;
        private final DataOutputStream output;

        private Session(Socket socket, DataInputStream input, DataOutputStream output) {
            this.socket = socket;
            this.input = input;
            this.output = output;
        }

        public DataOutputStream getOutput() {
            return output;
        }

        private void close() {
            SocketCloseUtil.closeAll(socket, input, output);
        }
    }

    private static class Sender implements Runnable {
        private final Session session;

        private Sender(Session session) {
            this.session = session;
        }

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            try {
                while (true) {
                    log("전송 문자:");
                    String toSend = scanner.nextLine();

                    session.getOutput().writeUTF(toSend);
                    log(toSend);
                }
            } catch (IOException e) {
                log(e);
            } finally {
                session.close();
            }
        }
    }
}
