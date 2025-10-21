package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;
import Network.User;

public class UserElementsCommand implements Command {
    CommandHandler commandHandler;

    public UserElementsCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("show_user_elements", this);
    }

    @Override
    public Response execute(User user,String arguments, RouteModel objectArg) {
        return commandHandler.showUserElements(user, arguments, objectArg);
    }
}