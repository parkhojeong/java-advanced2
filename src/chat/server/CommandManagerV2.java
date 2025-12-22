package chat.server;

import java.io.IOException;
import java.util.List;

public class CommandManagerV2 implements CommandManager{
    private static final String DELIMITER = "\\|";
    private final SessionManager sessionManager;

    public CommandManagerV2(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(String totalMessage, Session session) throws IOException {
        if(totalMessage.startsWith("/join")){
            String[] split = totalMessage.split(DELIMITER);
            String username = split[1];
            session.setUsername(username);
            sessionManager.sendAll(username + " joined");
        }
        else if(totalMessage.startsWith("/message")){
            String[] split = totalMessage.split(DELIMITER);
            String message = split[1];
            sessionManager.sendAll("[" + session.getUsername() + "] " + message);
        }
        else if(totalMessage.startsWith("/change")){
            String[] split = totalMessage.split(DELIMITER);
            String username = split[1];
            sessionManager.sendAll(session.getUsername() + " changed username to " + username);
            session.setUsername(username);
        }
        else if(totalMessage.startsWith("/users")){
            List<String> usernames = sessionManager.getAllUsername();
            StringBuilder sb = new StringBuilder();
            sb.append("users: ").append(usernames.size()).append("\n");
            for(String username : usernames){
                sb.append(" - ").append(username).append("\n");
            }
            session.send(sb.toString());
        }
        else if(totalMessage.startsWith("/exit")){
            throw new IOException("exit");
        }
        else {
            session.send("unknown command: " + totalMessage);
        }
    }
}
