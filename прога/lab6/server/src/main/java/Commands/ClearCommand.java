package Commands;

import CollectionObject.RouteModel;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;

public class ClearCommand implements Command{
    private CommandHandler commandHandler;

    public ClearCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("clear", this);
    }

    @Override
    public Response execute(String arguments, RouteModel objectArg) {
        return commandHandler.clear(arguments, objectArg);
    }
}