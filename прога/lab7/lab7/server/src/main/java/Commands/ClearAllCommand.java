package Commands;

import CollectionObject.RouteModel;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
import Network.User;

public class ClearAllCommand implements Command{
    private CommandHandler commandHandler;

    public ClearAllCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("clear_all", this);
    }

    @Override
    public Response execute(User user,String arguments, RouteModel objectArg) {
        return commandHandler.clearAll(user, arguments, objectArg);
    }
}