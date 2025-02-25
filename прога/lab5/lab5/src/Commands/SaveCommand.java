package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Этот класс представляет команду, которая используется для сохранения данных.
 */
public class SaveCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Создает новый объект SaveCommand с указанным обработчиком команд.
     *
     * @param commandHandler обработчик команд, который управляет сохранением данных
     */
    public SaveCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        // Регистрирует эту команду с указанным именем в списке команд
        ConsoleApp.commandList.put("save", this);
    }

    /*
     * Выполняет команду сохранения данных, вызывая у обработчика команд соответствующую функцию.
     *
     * @param arguments любые дополнительные аргументы, переданные с командой (опционально)
     */
    @Override
    public void execute(String arguments) {
        commandHandler.save(arguments);
    }
}
