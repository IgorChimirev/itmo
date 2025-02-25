package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Этот класс представляет команду, которая используется для отображения истории выполнения команд.
 */
public class HistoryCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Создает новый объект HistoryCommand с указанным обработчиком команд.
     *
     * @param commandHandler обработчик команд, который управляет историей команд
     */
    public HistoryCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        // Регистрирует эту команду с указанным именем в списке команд
        ConsoleApp.commandList.put("history", this);
    }

    /*
     * Выполняет команду истории, вызывая у обработчика команд отображение истории выполненных команд.
     *
     * @param arguments любые дополнительные аргументы, переданные с командой (опционально)
     */
    @Override
    public void execute(String arguments) {
        commandHandler.history(arguments);
    }
}
