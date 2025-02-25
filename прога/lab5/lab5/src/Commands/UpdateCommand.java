package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Этот класс представляет команду, которая используется для обновления данных.
 */
public class UpdateCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Создает новый объект UpdateCommand с указанным обработчиком команд.
     *
     * @param commandHandler обработчик команд, который управляет обновлением данных
     */
    public UpdateCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        // Регистрирует эту команду с указанным именем в списке команд
        ConsoleApp.commandList.put("update", this);
    }

    /*
     * Выполняет команду обновления данных, вызывая у обработчика команд соответствующую функцию.
     *
     * @param arguments строка аргументов, содержащая информацию для обновления данных
     */
    @Override
    public void execute(String arguments) {
        commandHandler.update(arguments);
    }
}
