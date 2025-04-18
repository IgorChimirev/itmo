/**package Commands;

import Modules.CommandHandler;
import Modules.ConsoleApp;

public class SaveCommand implements Command {
    private final CommandHandler commandHandler;

    public SaveCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        // Регистрация команды при создании экземпляра
        ConsoleApp.commandList.put("save", this);
    }

    @Override
    public void execute(String arguments) {
        // Проверка наличия аргументов
        if (!arguments.isEmpty()) {
            System.out.println("Ошибка: команда 'save' не принимает аргументов");
            return;
        }

        try {
            commandHandler.save(arguments);
            System.out.println("Коллекция успешно сохранена");
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении: " + e.getMessage());
        }
    }
}**/