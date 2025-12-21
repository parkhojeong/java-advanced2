package network.tcp.chat;

import network.tcp.SocketCloseUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.stream.Collectors;

import static util.MyLogger.log;

public class Session implements Runnable {
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final SessionManager sessionManager;
    private String name;
    private boolean closed = false;

    public Session(Socket socket, SessionManager sessionManager) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.sessionManager = sessionManager;
        sessionManager.add(this);
    }

    @Override
    public void run() {
        try {
            output.writeUTF("Welcome to the chat room!");

            while (true) {
                output.writeUTF("Type /join|your_name to enter the chat room");
                String receivedMessage = input.readUTF();
                if(receivedMessage.startsWith("/join|")) {
                    name = receivedMessage.substring(6);
                    send(name + ": enter the chat room");
                    break;
                }
            }

            while (true) {
                String receivedMessage = input.readUTF();
                log("[client -> server] received:" + receivedMessage);

                if(receivedMessage.startsWith("/users")){

                    String message = this.sessionManager.findAll().stream()
                            .map(session -> session.name)
                            .collect(Collectors.joining(", "));

                    send(message);
                }
                else if(receivedMessage.startsWith("/message|")) {
                    receivedMessage = receivedMessage.substring(9);

                    send(this.name + ": " + receivedMessage);
                }
                else if(receivedMessage.startsWith("/change")) {
                    this.name = receivedMessage.substring(8);
                }
                else if(receivedMessage.equals("/exit")) {
                    send(this.name + ": left the chat room");
                    break;
                }
            }

        } catch (IOException e) {
            log(e);
        } finally {
            sessionManager.remove(this);
            close();
        }
    }

    private void send(String message) throws IOException {
        for (Session session : this.sessionManager.findAll()) {
            session.output.writeUTF(message);
        }
    }

    public synchronized void close() {
        if (closed) {
            return;
        }

        SocketCloseUtil.closeAll(socket, input, output);
        closed = true;
        log("socket " + socket);
    }
}
