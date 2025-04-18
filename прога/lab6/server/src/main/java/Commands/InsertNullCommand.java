package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
public class InsertNullCommand implements Command {
    private CommandHandler commandHandler;

    public InsertNullCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("InsertNull", this);
    }

    @Override
    public Response execute(String arguments, RouteModel objectArg) {
        return commandHandler.insertNull(arguments, objectArg);
    }
}