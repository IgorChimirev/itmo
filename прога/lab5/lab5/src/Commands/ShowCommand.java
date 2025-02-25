package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Этот класс представляет команду, которая используется для отображения данных.
 */
public class ShowCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Создает новый объект ShowCommand с указанным обработчиком команд.
     *
     * @param commandHandler обработчик команд, который управляет отображением данных
     */
    public ShowCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        // Регистрирует эту команду с указанным именем в списке команд
        ConsoleApp.commandList.put("show", this);
    }

    /*
     * Выполняет команду отображения данных, вызывая у обработчика команд соответствующую функцию.
     *
     * @param arguments любые дополнительные аргументы, переданные с командой (опционально)
     */
    @Override
    public void execute(String arguments) {
        commandHandler.show(arguments);
    }
}
