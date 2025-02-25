import Commands.*;
import Modules.CSVProvider;
import Modules.ConsoleApp;
import Modules.CommandHandler;
import Modules.PromptScan;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Главный класс приложения для работы с коллекцией маршрутов.
 * Обеспечивает инициализацию системы и обработку пользовательского ввода.
 */
public class Main {

    /**
     * Точка входа в приложение
     * @param args аргументы командной строки:
     *             [0] - путь к файлу коллекции (опционально)
     */
    public static void main(String[] args) {
        // Инициализация приложения
        ConsoleApp consoleApp = createConsoleApp();

        // Загрузка коллекции из файла
        try {
            if (args.length > 0) {
                Path pathToCollection = Path.of(args[0]);
                CSVProvider csvProvider = new CSVProvider(pathToCollection);
                csvProvider.load();
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
            System.out.println("Файл коллекции не указан, работаем с пустой коллекцией");
        }

        // Настройка ввода
        PromptScan.setUserScanner(new Scanner(System.in));
        Scanner scanner = PromptScan.getUserScanner();

        // Приветствие пользователя
        printWelcomeMessage(consoleApp);

        // Главный цикл обработки команд
        processUserInput(scanner, consoleApp);
    }

    /**
     * Выводит приветственное сообщение и справку
     * @param consoleApp экземпляр консольного приложения
     */
    private static void printWelcomeMessage(ConsoleApp consoleApp) {
        System.out.println("Добро пожаловать в систему управления коллекцией маршрутов!");
        System.out.println("Доступные команды:");
        consoleApp.help("");
        System.out.print("\n> ");
    }

    /**
     * Обрабатывает пользовательский ввод
     * @param scanner сканер для чтения ввода
     * @param consoleApp экземпляр консольного приложения
     */
    private static void processUserInput(Scanner scanner, ConsoleApp consoleApp) {
        try {
            while (true) {
                if (scanner.hasNext()) {
                    String[] input = parseInput(scanner.nextLine());
                    handleCommand(input, consoleApp);
                    System.out.print("> ");
                }
            }
        } catch (NoSuchElementException e) {
            handleShutdown();
        }
    }

    /**
     * Разбирает введенную строку на команду и аргументы
     * @param inputLine строка ввода
     * @return массив из двух элементов: [команда, аргументы]
     */
    private static String[] parseInput(String inputLine) {
        String[] input = (inputLine + " ").trim().split(" ", 2);
        return new String[] {
                input[0].trim(),
                input.length > 1 ? input[1].trim() : ""
        };
    }

    /**
     * Выполняет команду
     * @param input массив с командой и аргументами
     * @param consoleApp экземпляр консольного приложения
     */
    private static void handleCommand(String[] input, ConsoleApp consoleApp) {
        String command = input[0];
        String arguments = input[1];

        if (ConsoleApp.commandList.containsKey(command)) {
            ConsoleApp.commandList.get(command).execute(arguments);
            CommandHandler.addCommand(command);
        } else {
            System.out.println("Неизвестная команда: '" + command + "'. Введите 'help' для списка команд");
        }
    }

    /**
     * Обрабатывает аварийное завершение работы
     */
    private static void handleShutdown() {
        System.out.println("\nОбнаружено аварийное завершение ввода");
        System.out.println("Завершение работы программы...");
        System.exit(1);
    }

    /**
     * Создает и конфигурирует экземпляр консольного приложения
     * @return настроенный экземпляр ConsoleApp
     */
    private static ConsoleApp createConsoleApp() {
        CommandHandler commandHandler = new CommandHandler();
        return new ConsoleApp(
                new HelpCommand(commandHandler),
                new InfoCommand(commandHandler),
                new ShowCommand(commandHandler),
                new AddCommand(commandHandler),
                new UpdateCommand(commandHandler),
                new RemoveByIdCommand(commandHandler),
                new ClearCommand(commandHandler),
                new SaveCommand(commandHandler),
                new ExecuteScriptCommand(commandHandler),
                new ExitCommand(commandHandler),
                new RemoveLowerCommand(commandHandler),
                new ReorderCommand(commandHandler),
                new HistoryCommand(commandHandler),
                new filter_less_than_distance(commandHandler),
                new FilterStartsWithNameCommand(commandHandler)
        );
    }
}