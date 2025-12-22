package chat.server.command;

import chat.server.Session;
import chat.server.SessionManager;


public class ChangeCommand implements Command{
    private final SessionManager sessionManager;

    public ChangeCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(String[] args, Session session) {
        String username = args[1];
        sessionManager.sendAll(session.getUsername() + " changed username to " + username);
        session.setUsername(username);
    }
}
