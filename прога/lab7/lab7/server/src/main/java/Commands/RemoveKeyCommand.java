package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
import Network.User;
public class RemoveKeyCommand implements Command {
    private CommandHandler commandHandler;

    public RemoveKeyCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("remove_key", this);
    }

    @Override
    public Response execute(User user,String arguments, RouteModel objectArg) {
        return commandHandler.removeKey(user,arguments, objectArg);
    }
}