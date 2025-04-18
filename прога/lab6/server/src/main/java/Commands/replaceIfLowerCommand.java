package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
public class replaceIfLowerCommand implements Command {
    private CommandHandler commandHandler;

    public replaceIfLowerCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("replace_if_lower", this);
    }

    @Override
    public Response execute(String arguments, RouteModel objectArg) {
        return commandHandler.replaceIfLower(arguments, objectArg);
    }
}