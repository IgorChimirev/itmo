package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Класс для реализации команды "add", которая добавляет элемент с помощью CommandHandler.
 */
public class AddCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Конструктор класса AddCommand, который инициализирует обработчик команд и регистрирует команду в приложении.
     *
     * @param commandHandler обработчик команд, используемый для выполнения логики добавления.
     */
    public AddCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("add", this); // Регистрирует команду "add".
    }

    /*
     * Выполняет команду "add", используя переданные аргументы.
     *
     * @param arguments аргументы команды, используемые для добавления нового элемента.
     */
    @Override
    public void execute(String arguments) {
        commandHandler.add(arguments); // Использует обработчик для выполнения добавления.
    }
}
