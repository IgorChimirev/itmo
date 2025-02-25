package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Этот класс представляет команду, которая используется для переупорядочивания элементов в определенной последовательности.
 */
public class ReorderCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Создает новый объект ReorderCommand с указанным обработчиком команд.
     *
     * @param commandHandler обработчик команд, который управляет переупорядочиванием элементов
     */
    public ReorderCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        // Регистрирует эту команду с указанным именем в списке команд
        ConsoleApp.commandList.put("reorder", this);
    }

    /*
     * Выполняет команду переупорядочивания элементов, вызывая у обработчика команд соответствующую функцию.
     *
     * @param arguments любые дополнительные аргументы, переданные с командой (опционально)
     */
    @Override
    public void execute(String arguments) {
        commandHandler.reorder(arguments);
    }
}
