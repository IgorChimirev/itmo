package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Класс для реализации команды "help", которая предоставляет справочную информацию о доступных командах.
 */
public class HelpCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Конструктор класса HelpCommand, который инициализирует обработчик команд
     * и регистрирует команду в приложении.
     *
     * @param commandHandler обработчик команд, используемый для предоставления информации о командах.
     */
    public HelpCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("help", this); // Регистрирует команду "help".
    }

    /*
     * Выполняет команду "help", вызывая метод помощи в обработчике команд.
     *
     * @param arguments аргументы команды, хотя для команды помощи аргументы обычно не требуются.
     */
    @Override
    public void execute(String arguments) {
        commandHandler.help(arguments); // Использует обработчик для выполнения команды помощи.
    }
}
