package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Класс для реализации команды "clear", которая очищает данные с помощью CommandHandler.
 */
public class ClearCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Конструктор класса ClearCommand, который инициализирует обработчик команд и регистрирует команду в приложении.
     *
     * @param commandHandler обработчик команд, используемый для выполнения логики очистки.
     */
    public ClearCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("clear", this); // Регистрирует команду "clear".
    }

    /*
     * Выполняет команду "clear", используя переданные аргументы.
     *
     * @param arguments аргументы команды, используемые для выполнения действия очистки.
     */
    @Override
    public void execute(String arguments) {
        commandHandler.clear(arguments); // Использует обработчик для выполнения очистки.
    }
}
