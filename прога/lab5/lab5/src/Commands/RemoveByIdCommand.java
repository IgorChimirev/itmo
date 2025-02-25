package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Этот класс представляет команду, которая используется для удаления элемента по его идентификатору.
 */
public class RemoveByIdCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Создает новый объект RemoveByIdCommand с указанным обработчиком команд.
     *
     * @param commandHandler обработчик команд, который управляет удалением элементов
     */
    public RemoveByIdCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        // Регистрирует эту команду с указанным именем в списке команд
        ConsoleApp.commandList.put("removeById", this);
    }

    /*
     * Выполняет команду удаления элемента по идентификатору, вызывая у обработчика команд соответствующую функцию.
     *
     * @param arguments строка аргументов, содержащая идентификатор элемента для удаления
     */
    @Override
    public void execute(String arguments) {
        commandHandler.removeById(arguments);
    }
}
