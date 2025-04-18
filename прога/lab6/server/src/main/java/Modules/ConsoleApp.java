package Modules;

import CollectionObject.RouteModel;
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
    private Command InsertNullCommand;
    private Command removeKey;
    private Command info;
    private Command show;
    private Command add;
    private Command update;
    private Command removeById;
    private Command replaceIfLower;
    private Command clear;
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
     * @param executeScript обработчик команды executeScript
     * @param exit обработчик команды exit
     * @param removeLower обработчик команды removeLower
     * @param reorder обработчик команды reorder
     * @param history обработчик команды history
     * @param filter_less_than_distance обработчик команды filter_less_than_distance
     * @param filterStartsWithName обработчик команды filterStartsWithName
     */
    public ConsoleApp(Command help, Command info, Command show, Command add, Command update, Command removeById,
                      Command clear, Command executeScript, Command exit, Command removeLower,
                      Command reorder, Command history,Command InsertNullCommand, Command filter_less_than_distance,
                      Command filterStartsWithName,Command removeKey, Command replaceIfLower) {
        this.help = help;
        this.info = info;
        this.show = show;
        this.add = add;
        this.replaceIfLower = replaceIfLower;
        this.update = update;
        this.removeById = removeById;
        this.clear = clear;
        this.executeScript = executeScript;
        this.InsertNullCommand = InsertNullCommand;
        this.removeKey = removeKey;
        this.exit = exit;
        this.removeLower = removeLower;
        this.reorder = reorder;
        this.history = history;
        this.filter_less_than_distance = filter_less_than_distance;
        this.filterStartsWithName = filterStartsWithName;
    }
    public void replaceIfLower(String arguments, RouteModel objectArg) {replaceIfLower.execute(arguments, objectArg);
    }
    /**
     * Выполняет команду help
     * @param arguments аргументы команды
     */
    public void help(String arguments, RouteModel objectArg) {
        help.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду info
     * @param arguments аргументы команды
     */
    public void info(String arguments, RouteModel objectArg) {
        info.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду show
     * @param arguments аргументы команды
     */
    public void show(String arguments, RouteModel objectArg) {
        show.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду add
     * @param arguments аргументы команды
     */
    public void add(String arguments, RouteModel objectArg) {
        add.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду update
     * @param arguments аргументы команды (требуется ID элемента)
     */
    public void update(String arguments, RouteModel objectArg) {
        update.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду removeById
     * @param arguments аргументы команды (требуется ID элемента)
     */
    public void removeById(String arguments, RouteModel objectArg) {
        removeById.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду clear
     * @param arguments аргументы команды
     */
    public void clear(String arguments, RouteModel objectArg) {
        clear.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду save
     * @param arguments аргументы команды
     */


    /**
     * Выполняет команду executeScript
     * @param arguments аргументы команды (требуется путь к скрипту)
     */
    public void executeScript(String arguments, RouteModel objectArg) {
        executeScript.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду removeGreater (реализация через removeLower)
     * @param arguments аргументы команды
     */
    public void removeGreater(String arguments, RouteModel objectArg) {
        removeLower.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду reorder
     * @param arguments аргументы команды
     */
    public void reorder(String arguments, RouteModel objectArg) {
        reorder.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду history
     * @param arguments аргументы команды
     */
    public void history(String arguments, RouteModel objectArg) {
        history.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду removeAllByType
     * @param arguments аргументы команды (требуется тип элемента)
     */
    public void removeAllByType(String arguments, RouteModel objectArg) {
        removeAllByType.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду filter_less_than_distance
     * @param arguments аргументы команды (требуется значение расстояния)
     */
    public void filter_less_than_distance(String arguments, RouteModel objectArg) {
        filter_less_than_distance.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду filterStartsWithName
     * @param arguments аргументы команды (требуется подстрока для поиска)
     */
    public void filterStartsWithName(String arguments, RouteModel objectArg) {
        filterStartsWithName.execute(arguments, objectArg);
    }

    /**
     * Выполняет команду exit
     * @param arguments аргументы команды
     */
    public void exit(String arguments, RouteModel objectArg) {
        exit.execute(arguments, objectArg);
    }
    public void InsertNullCommand(String arguments, RouteModel objectArg) {
        InsertNullCommand.execute(arguments, objectArg);
    }
    public void removeKey(String arguments, RouteModel objectArg) {
        removeKey.execute(arguments, objectArg);
    }
}