package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
import Network.User;

public class PrintFieldDescendingDistance implements Command {
    CommandHandler commandHandler;

    public PrintFieldDescendingDistance(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("PrintFieldDescendingDistance", this);
    }

    @Override
    public Response execute(User user,String arguments, RouteModel objectArg) {
        return commandHandler.filterStartsWithName(user, arguments, objectArg);
    }
}