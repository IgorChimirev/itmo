package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;

public class HelpCommand implements Command {
    CommandHandler commandHandler;

    public HelpCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("help", this);
    }

    @Override
    public Response execute(String arguments, RouteModel objectArg) {
        return commandHandler.help(arguments, objectArg);
    }
}