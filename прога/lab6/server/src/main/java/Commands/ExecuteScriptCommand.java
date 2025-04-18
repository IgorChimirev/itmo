package Commands;

import CollectionObject.*;
import Modules.CommandHandler;
import Modules.ConsoleApp;
import Network.Response;

/*
 * Класс для реализации команды "execute_script", которая выполняет скрипты.
 */
public class ExecuteScriptCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Конструктор класса ExecuteScriptCommand.
     *
     * @param commandHandler обработчик команд.
     */
    public ExecuteScriptCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("execute_script", this);
    }

    /*
     * Выполняет команду "execute_script".
     *
     * @param arguments путь к файлу скрипта.
     * @param objectArg игнорируется (для совместимости с интерфейсом).
     * @return ответ с результатом выполнения.
     */
    @Override
    public Response execute(String arguments, RouteModel objectArg) {
        return commandHandler.executeScript(arguments, objectArg);
    }
}