package Modules;

import Commands.Command;
import java.util.HashMap;

/**
 * Основной класс консольного приложения для управления командами.
 * Обеспечивает связь между пользовательскими командами и их обработчиками.
 */
public class ConsoleApp {
    /**
     * Список доступных команд в формате: Имя команды -> Объект команды
     */
    public static HashMap<String, Command> commandList = new HashMap<>();

    // Приватные поля для хранения обработчиков команд
    private Command help;
    private Command info;
    private Command show;
    private Command add;
    private Command update;
    private Command removeById;
    private Command clear;
    private Command save;
    private Command executeScript;
    private Command exit;
    private Command removeLower;
    private Command reorder;
    private Command history;
    private Command removeAllByType;
    private Command filter_less_than_distance;
    private Command filterStartsWithName;

    /**
     * Конструктор инициализирует обработчики для всех команд приложения
     * @param help обработчик команды help
     * @param info обработчик команды info
     * @param show обработчик команды show
     * @param add обработчик команды add
     * @param update обработчик команды update
     * @param removeById обработчик команды removeById
     * @param clear обработчик команды clear
     * @param save обработчик команды save
     * @param executeScript обработчик команды executeScript
     * @param exit обработчик команды exit
     * @param removeLower обработчик команды removeLower
     * @param reorder обработчик команды reorder
     * @param history обработчик команды history
     * @param filter_less_than_distance обработчик команды filter_less_than_distance
     * @param filterStartsWithName обработчик команды filterStartsWithName
     */
    public ConsoleApp(Command help, Command info, Command show, Command add, Command update, Command removeById,
                      Command clear, Command save, Command executeScript, Command exit, Command removeLower,
                      Command reorder, Command history, Command filter_less_than_distance,
                      Command filterStartsWithName) {
        this.help = help;
        this.info = info;
        this.show = show;
        this.add = add;
        this.update = update;
        this.removeById = removeById;
        this.clear = clear;
        this.save = save;
        this.executeScript = executeScript;
        this.exit = exit;
        this.removeLower = removeLower;
        this.reorder = reorder;
        this.history = history;
        this.filter_less_than_distance = filter_less_than_distance;
        this.filterStartsWithName = filterStartsWithName;
    }

    /**
     * Выполняет команду help
     * @param arguments аргументы команды
     */
    public void help(String arguments) {
        help.execute(arguments);
    }

    /**
     * Выполняет команду info
     * @param arguments аргументы команды
     */
    public void info(String arguments) {
        info.execute(arguments);
    }

    /**
     * Выполняет команду show
     * @param arguments аргументы команды
     */
    public void show(String arguments) {
        show.execute(arguments);
    }

    /**
     * Выполняет команду add
     * @param arguments аргументы команды
     */
    public void add(String arguments) {
        add.execute(arguments);
    }

    /**
     * Выполняет команду update
     * @param arguments аргументы команды (требуется ID элемента)
     */
    public void update(String arguments) {
        update.execute(arguments);
    }

    /**
     * Выполняет команду removeById
     * @param arguments аргументы команды (требуется ID элемента)
     */
    public void removeById(String arguments) {
        removeById.execute(arguments);
    }

    /**
     * Выполняет команду clear
     * @param arguments аргументы команды
     */
    public void clear(String arguments) {
        clear.execute(arguments);
    }

    /**
     * Выполняет команду save
     * @param arguments аргументы команды
     */
    public void save(String arguments) {
        save.execute(arguments);
    }

    /**
     * Выполняет команду executeScript
     * @param arguments аргументы команды (требуется путь к скрипту)
     */
    public void executeScript(String arguments) {
        executeScript.execute(arguments);
    }

    /**
     * Выполняет команду removeGreater (реализация через removeLower)
     * @param arguments аргументы команды
     */
    public void removeGreater(String arguments) {
        removeLower.execute(arguments);
    }

    /**
     * Выполняет команду reorder
     * @param arguments аргументы команды
     */
    public void reorder(String arguments) {
        reorder.execute(arguments);
    }

    /**
     * Выполняет команду history
     * @param arguments аргументы команды
     */
    public void history(String arguments) {
        history.execute(arguments);
    }

    /**
     * Выполняет команду removeAllByType
     * @param arguments аргументы команды (требуется тип элемента)
     */
    public void removeAllByType(String arguments) {
        removeAllByType.execute(arguments);
    }

    /**
     * Выполняет команду filter_less_than_distance
     * @param arguments аргументы команды (требуется значение расстояния)
     */
    public void filter_less_than_distance(String arguments) {
        filter_less_than_distance.execute(arguments);
    }

    /**
     * Выполняет команду filterStartsWithName
     * @param arguments аргументы команды (требуется подстрока для поиска)
     */
    public void filterStartsWithName(String arguments) {
        filterStartsWithName.execute(arguments);
    }

    /**
     * Выполняет команду exit
     * @param arguments аргументы команды
     */
    public void exit(String arguments) {
        exit.execute(arguments);
    }
}