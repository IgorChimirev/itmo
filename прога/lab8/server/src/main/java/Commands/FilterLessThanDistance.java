package Commands;

import CollectionObject.RouteModel;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
import Network.User;

public class FilterLessThanDistance implements Command{
    private CommandHandler commandHandler;

    public FilterLessThanDistance(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("filter_less_than_distance", this);
    }

    @Override
    public Response execute(User user,String arguments, RouteModel objectArg) {
        return commandHandler.filter_less_than_distance(user,arguments, objectArg);
    }
}