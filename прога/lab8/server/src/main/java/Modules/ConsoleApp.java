package Modules;

import CollectionObject.RouteModel;
import Commands.Command;
import Network.Response;
import Network.User;

import java.util.HashMap;

public class ConsoleApp {
    // хэшмапа команд. Ключ - имя команды; Значение - класс-оболочка команды
    public static HashMap<String, Command> commandList = new HashMap<>();
    private Command help;
    private Command InsertNullCommand;
    private Command FilterCommand;
    private Command ShowUserElementsCommand;
    private Command removeKey;
    private Command info;
    private Command clear;
    private Command ClearAllCommand;
    private Command show;
    private Command RandomCommand;
    private Command add;
    private Command update;
    private Command removeById;
    private Command replaceIfLower;
    private Command removeLower;
    private Command reorder;
    private Command history;
    private Command removeAllByType;
    private Command filter_less_than_distance;
    private Command FilterStartsWithNameCommand;

    public ConsoleApp(Command help, Command info, Command show, Command add, Command update, Command removeById,
                      Command clear, Command removeLower,
                      Command reorder, Command history,Command InsertNullCommand, Command filter_less_than_distance,
                      Command FilterStartsWithNameCommand,Command removeKey,Command ClearAllCommand, Command replaceIfLower, Command RandomCommand,Command FilterCommand,Command ShowUserElementsCommand) {
        this.help = help;
        this.info = info;
        this.show = show;
        this.add = add;
        this.ShowUserElementsCommand = ShowUserElementsCommand;
        this.FilterCommand = FilterCommand;
        this.ClearAllCommand = ClearAllCommand;
        this.replaceIfLower = replaceIfLower;
        this.update = update;
        this.RandomCommand = RandomCommand;
        this.removeById = removeById;
        this.clear = clear;
        this.InsertNullCommand = InsertNullCommand;
        this.removeKey = removeKey;
        this.removeLower = removeLower;
        this.reorder = reorder;
        this.history = history;
        this.filter_less_than_distance = filter_less_than_distance;
        this.FilterStartsWithNameCommand = FilterStartsWithNameCommand;
    }
    public void replaceIfLower(User user,String arguments, RouteModel objectArg) {replaceIfLower.execute(user,arguments, objectArg);
    }
    /**
     * Выполняет команду help
     * @param arguments аргументы команды
     */
    public void help(User user,String arguments, RouteModel objectArg) {
        help.execute(user,arguments, objectArg);
    }
    public void ClearAllCommand(User user,String arguments, RouteModel objectArg) {
        ClearAllCommand.execute(user,arguments, objectArg);
    }
    public void FilterCommand(User user,String arguments, RouteModel objectArg) {
        FilterCommand.execute(user,arguments, objectArg);
    }
    public void ShowUserElementsCommand(User user,String arguments, RouteModel objectArg) {
        ShowUserElementsCommand.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду info
     * @param arguments аргументы команды
     */
    public void info(User user,String arguments, RouteModel objectArg) {
        info.execute(user,arguments, objectArg);
    }
    public void RandomCommand(User user,String arguments, RouteModel objectArg) {
        RandomCommand.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду show
     * @param arguments аргументы команды
     */
    public void show(User user,String arguments, RouteModel objectArg) {
        show.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду add
     * @param arguments аргументы команды
     */
    public void add(User user,String arguments, RouteModel objectArg) {
        add.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду update
     * @param arguments аргументы команды (требуется ID элемента)
     */
    public void update(User user,String arguments, RouteModel objectArg) {
        update.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду removeById
     * @param arguments аргументы команды (требуется ID элемента)
     */
    public void removeById(User user,String arguments, RouteModel objectArg) {
        removeById.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду clear
     * @param arguments аргументы команды
     */
    public void clear(User user,String arguments, RouteModel objectArg) {
        clear.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду save
     * @param arguments аргументы команды
     */


    /**
     * Выполняет команду executeScript
     * @param arguments аргументы команды (требуется путь к скрипту)
     */

    /**
     * Выполняет команду removeGreater (реализация через removeLower)
     * @param arguments аргументы команды
     */
    public void removeGreater(User user,String arguments, RouteModel objectArg) {
        removeLower.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду reorder
     * @param arguments аргументы команды
     */
    public void reorder(User user,String arguments, RouteModel objectArg) {
        reorder.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду history
     * @param arguments аргументы команды
     */
    public void history(User user,String arguments, RouteModel objectArg) {
        history.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду removeAllByType
     * @param arguments аргументы команды (требуется тип элемента)
     */
    public void removeAllByType(User user,String arguments, RouteModel objectArg) {
        removeAllByType.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду filter_less_than_distance
     * @param arguments аргументы команды (требуется значение расстояния)
     */
    public void filter_less_than_distance(User user,String arguments, RouteModel objectArg) {
        filter_less_than_distance.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду filterStartsWithName
     * @param arguments аргументы команды (требуется подстрока для поиска)
     */
    public void filterStartsWithName(User user,String arguments, RouteModel objectArg) {
        FilterStartsWithNameCommand.execute(user,arguments, objectArg);
    }

    /**
     * Выполняет команду exit
     * @param arguments аргументы команды
     */

    public void InsertNullCommand(User user,String arguments, RouteModel objectArg) {
        InsertNullCommand.execute(user,arguments, objectArg);
    }
    public void removeKey(User user,String arguments, RouteModel objectArg) {
        removeKey.execute(user,arguments, objectArg);
    }
}
