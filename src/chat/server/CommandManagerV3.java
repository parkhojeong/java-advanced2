package chat.server;

import chat.server.command.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandManagerV3 implements CommandManager {
    private static final String DELIMITER = "\\|";
    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandManagerV3(SessionManager sessionManager) {
        commandMap.put("/join", new JoinCommand(sessionManager));
        commandMap.put("/message", new MessageCommand(sessionManager));
        commandMap.put("/change", new ChangeCommand(sessionManager));
        commandMap.put("/users", new UsersCommand(sessionManager));
        commandMap.put("/exit", new ExitCommand(sessionManager));
    }


    @Override
    public void execute(String totalMessage, Session session) throws IOException {
        String[] args = totalMessage.split(DELIMITER);

        Command command = commandMap.get(args[0]);
        if(command == null){
            session.send("unknown command: " + totalMessage);
            return;
        }
        command.execute(args, session);
    }
}
