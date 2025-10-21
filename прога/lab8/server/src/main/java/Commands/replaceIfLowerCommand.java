package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
import Network.User;
public class replaceIfLowerCommand implements Command {
    private CommandHandler commandHandler;

    public replaceIfLowerCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("replace_if_lower", this);
    }

    @Override
    public Response execute(User user,String arguments, RouteModel objectArg) {
        return commandHandler.replaceIfLower(user, arguments, objectArg);
    }
}