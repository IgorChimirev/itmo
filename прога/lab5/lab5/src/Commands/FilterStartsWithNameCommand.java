package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Класс для реализации команды "filterStartsWithName", которая фильтрует элементы,
 * название которых начинается с указанной строки.
 */
public class FilterStartsWithNameCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Конструктор класса FilterStartsWithNameCommand, который инициализирует обработчик команд
     * и регистрирует команду в приложении.
     *
     * @param commandHandler обработчик команд, используемый для выполнения фильтрации по наименованию.
     */
    public FilterStartsWithNameCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("filterStartsWithName", this); // Регистрирует команду "filterStartsWithName".
    }

    /*
     * Выполняет команду "filterStartsWithName", используя переданные аргументы.
     *
     * @param arguments аргументы команды, которые содержат строку для фильтрации по началу наименования.
     */
    @Override
    public void execute(String arguments) {
        commandHandler.filterStartsWithName(arguments); // Использует обработчик для выполнения фильтрации.
    }
}
