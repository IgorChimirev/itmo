package Commands;

import CollectionObject.RouteModel;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
import Network.User;

public class FilterStartsWithNameCommand implements Command{
    private CommandHandler commandHandler;

    public FilterStartsWithNameCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("filterStartsWithName", this);
    }

    @Override
    public Response execute(User user, String arguments, RouteModel objectArg) {
        return commandHandler.filterStartsWithName(user,arguments, objectArg);
    }
}
