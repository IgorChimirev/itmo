package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

/*
 * Класс для реализации команды "exit", которая завершает работу приложения.
 */
public class ExitCommand implements Command {
    private CommandHandler commandHandler;

    /*
     * Конструктор класса ExitCommand инициализирует обработчик команд и регистрирует команду в приложении.
     *
     * @param commandHandler обработчик команд, используемый для выполнения логики завершения работы приложения.
     */
    public ExitCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("exit", this); // Регистрирует команду "exit".
    }

    /*
     * Выполняет команду "exit", вызывая метод выхода в обработчике команд.
     *
     * @param arguments аргументы, которые могут быть использованы для выполнения команды, хотя обычно для выхода они не требуются.
     */
    @Override
    public void execute(String arguments) {
        commandHandler.exit(arguments); // Использует обработчик для выполнения процедуры завершения работы.
    }
}
