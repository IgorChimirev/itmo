package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
import Network.User;

public class FilterCommand implements Command {
    CommandHandler commandHandler;

    public FilterCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("filter_by_user", this);
    }

    @Override
    public Response execute(User user,String arguments, RouteModel objectArg) {
        return commandHandler.filterByUser(user,arguments, objectArg);
    }
}