package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Класс для реализации команды "executeScript", которая выполняет скрипты с использованием CommandHandler.
 */
public class ExecuteScriptCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Конструктор класса ExecuteScriptCommand, который инициализирует обработчик команд и регистрирует команду в приложении.
     *
     * @param commandHandler обработчик команд, используемый для выполнения логики выполнения скриптов.
     */
    public ExecuteScriptCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("executeScript", this); // Регистрирует команду "executeScript".
    }

    /*
     * Выполняет команду "executeScript", используя переданные аргументы.
     *
     * @param arguments аргументы команды, например, путь к файлу скрипта для выполнения.
     */
    @Override
    public void execute(String arguments) {
        commandHandler.executeScript(arguments); // Использует обработчик для выполнения скрипта.
    }
}
