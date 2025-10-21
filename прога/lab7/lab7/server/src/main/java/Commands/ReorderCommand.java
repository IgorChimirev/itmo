package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
import Network.User;
public class ReorderCommand implements Command {
    CommandHandler commandHandler;

    public ReorderCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("reorder", this);
    }

    @Override
    public Response execute(User user,String arguments, RouteModel objectArg) {
        return commandHandler.help(user,arguments, objectArg);
    }
}