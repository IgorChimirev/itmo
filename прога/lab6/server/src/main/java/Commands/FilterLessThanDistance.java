package Commands;

import CollectionObject.RouteModel;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;

public class FilterLessThanDistance implements Command{
    private CommandHandler commandHandler;

    public FilterLessThanDistance(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("filter_less_than_distance", this);
    }

    @Override
    public Response execute(String arguments, RouteModel objectArg) {
        return commandHandler.filter_less_than_distance(arguments, objectArg);
    }
}