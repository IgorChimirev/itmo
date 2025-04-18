package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
public class RemoveKeyCommand implements Command {
    private CommandHandler commandHandler;

    public RemoveKeyCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("remove_key", this);
    }

    @Override
    public Response execute(String arguments, RouteModel objectArg) {
        return commandHandler.removeKey(arguments, objectArg);
    }
}