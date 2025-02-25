package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Класс для реализации команды "filter_less_than_distance", которая фильтрует элементы,
 * расстояние которых меньше указанного значения.
 */
public class filter_less_than_distance implements Command {
    private CommandHandler commandHandler;

    /*
     * Конструктор класса filter_less_than_distance, который инициализирует обработчик команд
     * и регистрирует команду в приложении.
     *
     * @param commandHandler обработчик команд, используемый для выполнения фильтрации по дистанции.
     */
    public filter_less_than_distance(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("filter_less_than_distance", this); // Регистрирует команду "filter_less_than_distance".
    }

    /*
     * Выполняет команду "filter_less_than_distance", используя переданные аргументы.
     *
     * @param arguments аргументы команды, которые содержат значение дистанции для фильтрации.
     */
    @Override
    public void execute(String arguments) {
        commandHandler.filter_less_than_distance(arguments); // Использует обработчик для выполнения фильтрации.
    }
}