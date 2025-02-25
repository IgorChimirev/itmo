package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Этот класс представляет команду, которая используется для отображения информации о приложении или других данных.
 */
public class InfoCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Создает новый объект InfoCommand с указанным обработчиком команд.
     *
     * @param commandHandler обработчик команд, который управляет выводом информации
     */
    public InfoCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        // Регистрирует эту команду с указанным именем в списке команд
        ConsoleApp.commandList.put("info", this);
    }

    /*
     * Выполняет команду информации, вызывая у обработчика команд отображение информации.
     *
     * @param arguments любые дополнительные аргументы, переданные с командой (опционально)
     */
    @Override
    public void execute(String arguments) {
        commandHandler.info(arguments);
    }
}
