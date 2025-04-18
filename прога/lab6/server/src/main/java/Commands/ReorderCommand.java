package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
public class ReorderCommand implements Command {
    CommandHandler commandHandler;

    public ReorderCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("reorder", this);
    }

    @Override
    public Response execute(String arguments, RouteModel objectArg) {
        return commandHandler.help(arguments, objectArg);
    }
}