package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;

public class AddCommand implements Command {
    private CommandHandler commandHandler;

    public AddCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("add", this);
    }

    @Override
    public Response execute(String arguments, RouteModel objectArg) {
        // Сначала конвертируем RouteModel в Route
        Route route = RouteConverter.convert(objectArg);

        // Передаем конвертированный объект в commandHandler
        return commandHandler.add(arguments, route);
    }
}