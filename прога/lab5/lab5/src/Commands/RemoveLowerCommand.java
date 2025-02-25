package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Этот класс представляет команду, которая используется для удаления всех элементов, меньших заданного.
 */
public class RemoveLowerCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Создает новый объект RemoveLowerCommand с указанным обработчиком команд.
     *
     * @param commandHandler обработчик команд, который управляет удалением элементов
     */
    public RemoveLowerCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        // Регистрирует эту команду с указанным именем в списке команд
        ConsoleApp.commandList.put("removeLower", this);
    }

    /*
     * Выполняет команду удаления всех элементов, меньших указанного, вызывая у обработчика команд соответствующую функцию.
     *
     * @param arguments строка аргументов, определяющая критерий для сравнения элементов
     */
    @Override
    public void execute(String arguments) {
        commandHandler.removeLower(arguments);
    }
}
