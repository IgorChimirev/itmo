package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
public class RemoveLowerCommand implements Command {
    private CommandHandler commandHandler;

    public RemoveLowerCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("remove_lower", this);
    }

    @Override
    public Response execute(String arguments, RouteModel objectArg) {
        return commandHandler.removeLower(arguments, objectArg);
    }
}