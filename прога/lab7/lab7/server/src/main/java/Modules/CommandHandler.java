package Modules;

import CollectionObject.*;
import Exceptions.*;
import Network.Response;
import Network.User;
import java.io.FileNotFoundException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CommandHandler {
    private CollectionService collectionService;
    private static Hashtable<Integer, String> commandHistory = new Hashtable<>();
    private static int commandCounter = 0;
    private static Set<Path> scriptsNames = new TreeSet<>();

    public CommandHandler() {
        this.collectionService = new CollectionService();
    }

    // ================================= Основные команды =================================
    public synchronized Response help(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }

        String helpMessage = """
            ======== Доступные команды ========
            help - справка
            clear_all - очистить всю коллекцию (только для admin)
            show_user_elements <username> - показать элементы пользователя
            filter_by_user - показать ваши элементы
            add_random - добавить 1000 случайных элементов
            info - информация о коллекции
            show - показать элементы
            add - добавить элемент
            insertNull <key> - добавить по ключу
            remove_key <key> - удалить по ключу
            update <id> - обновить элемент
            removeById <id> - удалить по ID
            clear - очистить коллекцию
            execute_script <path> - выполнить скрипт
            exit - выход без сохранения
            replaceIfLower <key> <distance> - заменить если меньше
            removeLower <id> - удалить меньшие элементы
            reorder - обратная сортировка
            history - история команд
            filter_less_than_distance <distance> - фильтр по дистанции
            filterStartsWithName <prefix> - фильтр по имени
            ===================================""";
        addCommand("help");
        return new Response(helpMessage, "");
    }

    public synchronized Response info(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        addCommand("info");
        return new Response(collectionService.info(), collectionService.show().toString());
    }
    public synchronized Response clearAll(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: команда не требует аргументов", "");
        }
        if (!user.getUsername().equals("admin")) {
            return new Response("Ошибка: недостаточно прав", "");
        }
        try {
            collectionService.clearAll();
            addCommand("clear_all");
            return new Response("Коллекция полностью очищена", "");
        } catch (DBProviderException e) {
            return new Response("Ошибка БД: " + e.getMessage(), "");
        }
    }
    public synchronized Response showUserElements(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Используйте: show_user_elements <username>", "");
        }

        try {
            List<Route> userRoutes = collectionService.filterByUsername(strArgument);
            addCommand("show_user_elements " + strArgument);
            return userRoutes.isEmpty()
                    ? new Response("У пользователя " + strArgument + " нет элементов", "")
                    : new Response("Элементы пользователя " + strArgument + ":", userRoutes.toString());
        } catch (Exception e) {
            return new Response("Ошибка: " + e.getMessage(), "");
        }
    }

    public synchronized Response show(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        addCommand("show");
        return CollectionService.collection.isEmpty()
                ? new Response("Коллекция пуста", "")
                : new Response("Содержимое коллекции:", collectionService.show().toString());
    }
    public synchronized Response filterByUser(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: команда не требует аргументов", "");
        }
        try {
            List<Route> userRoutes = collectionService.filterByUser(user.getUsername());
            addCommand("filter_by_user");
            return userRoutes.isEmpty()
                    ? new Response("У вас нет элементов в коллекции", "")
                    : new Response("Ваши элементы:", userRoutes.toString());
        } catch (Exception e) {
            return new Response("Ошибка: " + e.getMessage(), "");
        }
    }

    // ============================== Методы модификации ==============================
    public synchronized Response insertNull(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument == null) {
            return new Response("Требуются ключ и объект", "");
        }

        try {
            long key = Long.parseLong(strArgument);
            collectionService.insertElement(key, objArgument, user);
            addCommand("insertNull " + key);
            return new Response("Элемент добавлен", collectionService.show().toString());
        } catch (NumberFormatException e) {
            return new Response("Неверный формат ключа", "");
        } catch (NonExistingElementException | DBProviderException e) {
            return new Response(e.getMessage(), "");
        }
    }

    public synchronized Response removeKey(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Требуется ключ", "");
        }

        try {
            long key = Long.parseLong(strArgument);
            collectionService.removeByKey(key, user);
            addCommand("remove_key " + key);
            return new Response("Элемент удалён", collectionService.show().toString());
        } catch (NumberFormatException e) {
            return new Response("Неверный формат ключа", "");
        } catch (NonExistingElementException | DBProviderException e) {
            return new Response(e.getMessage(), "");
        }
    }

    public synchronized Response add(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument == null) {
            return new Response("Требуется объект", "");
        }
        try {
            collectionService.add(objArgument, user);
            addCommand("add");
            return new Response("Элемент добавлен", collectionService.show().toString());
        } catch (DBProviderException e) {
            return new Response("Ошибка БД: " + e.getMessage(), "");
        }
    }

    public synchronized Response update(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument == null) {
            return new Response("Требуется ID и объект", "");
        }
        try {
            long id = Long.parseLong(strArgument);
            collectionService.update(id, objArgument, user);
            addCommand("update " + id);
            return new Response("Элемент обновлён", collectionService.show().toString());
        } catch (NumberFormatException | NonExistingElementException | DBProviderException e) {
            return new Response("Ошибка: " + e.getMessage(), "");
        }
    }

    public synchronized Response removeById(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Требуется ID", "");
        }
        try {
            long id = Long.parseLong(strArgument);
            collectionService.removeById(id, user);
            addCommand("removeById " + id);
            return new Response("Элемент удалён", collectionService.show().toString());
        } catch (NumberFormatException | NonExistingElementException | DBProviderException e) {
            return new Response("Ошибка: " + e.getMessage(), "");
        }
    }

    public synchronized Response clear(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        try {
            collectionService.clear(user);
            addCommand("clear");
            return new Response("Коллекция очищена", "");
        } catch (DBProviderException e) {
            return new Response("Ошибка БД: " + e.getMessage(), "");
        }
    }

    // ============================== Работа с скриптами ==============================
    public synchronized Response executeScript(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Используйте: execute_script <file_path>", "");
        }

        try {
            Path path = Paths.get(strArgument);
            Path scriptFile = path.getFileName();

            if (scriptsNames.contains(scriptFile)) {
                throw new ScriptRecursionException("Рекурсивный вызов запрещён");
            }

            scriptsNames.add(scriptFile);

            try (Scanner scanner = new Scanner(path)) {
                StringBuilder output = new StringBuilder();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split(" ", 2);
                    String command = parts[0];
                    String args = parts.length > 1 ? parts[1] : "";

                    Response response;
                    switch (command.toLowerCase()) {
                        case "execute_script":
                            response = executeScript(user, args, null);
                            break;
                        default:
                            response = ConsoleApp.commandList.get(command).execute(user, args, null);
                            break;
                    }
                    output.append(response.getMessage()).append("\n");
                }
                return new Response("Скрипт выполнен:\n" + output, "");
            } finally {
                scriptsNames.remove(scriptFile);
            }
        } catch (FileNotFoundException e) {
            return new Response("Файл не найден: " + strArgument, "");
        } catch (ScriptRecursionException e) {
            return new Response(e.getMessage(), "");
        } catch (Exception e) {
            return new Response("Ошибка выполнения: " + e.getMessage(), "");
        }
    }

    // ============================== Дополнительные команды ==============================
    public synchronized Response replaceIfLower(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Требуется ключ и дистанция", "");
        }
        String[] args = strArgument.split(" ");
        if (args.length != 2) {
            return new Response("Неверный формат", "");
        }
        try {
            long key = Long.parseLong(args[0]);
            double distance = Double.parseDouble(args[1]);
            collectionService.replaceIfLower(key, distance, user);
            addCommand("replaceIfLower " + key);
            return new Response("Элемент обновлён", collectionService.show().toString());
        } catch (NumberFormatException | DBProviderException e) {
            return new Response(e.getMessage(), "");
        }
    }

    public synchronized Response removeLower(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Требуется ID", "");
        }
        try {
            long id = Long.parseLong(strArgument);
            collectionService.removeLower(id, user);
            addCommand("removeLower " + id);
            return new Response("Элементы удалены", collectionService.show().toString());
        } catch (NumberFormatException | DBProviderException e) {
            return new Response(e.getMessage(), "");
        }
    }

    public synchronized Response reorder(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        addCommand("reorder");
        return new Response("Коллекция отсортирована", collectionService.reorder().toString());
    }

    public synchronized Response filter_less_than_distance(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Требуется дистанция", "");
        }
        try {
            double distance = Double.parseDouble(strArgument);
            var result = collectionService.filter_less_than_distance(distance);
            addCommand("filter_less_than_distance " + distance);
            return new Response("Результаты:", result.toString());
        } catch (NumberFormatException e) {
            return new Response("Неверный формат", "");
        }
    }

    public synchronized Response filterStartsWithName(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Требуется префикс", "");
        }
        try {
            var result = collectionService.filterStartsWithName(strArgument);
            addCommand("filterStartsWithName " + strArgument);
            return result.isEmpty()
                    ? new Response("Элементы не найдены", "")
                    : new Response("Результаты:", result.toString());
        } catch (NonExistingElementException e) {
            return new Response(e.getMessage(), "");
        }
    }

    // ============================== История команд ==============================
    public synchronized Response history(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        addCommand("history");
        StringBuilder history = new StringBuilder("Последние 7 команд:\n");
        commandHistory.values().forEach(cmd -> history.append(cmd).append("\n"));
        return new Response(history.toString(), "");
    }
    public synchronized Response add_random(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: команда не требует аргументов", "");
        }
        try {
            collectionService.addRandomElements(user);
            addCommand("add_random");
            return new Response("Добавлено 1000 случайных элементов", collectionService.show().toString());
        } catch (DBProviderException e) {
            return new Response("Ошибка БД: " + e.getMessage(), "");
        }
    }

    public static synchronized void addCommand(String command) {
        commandCounter++;
        commandHistory.put(commandCounter, command);
        if (commandHistory.size() > 7) {
            commandHistory.remove(commandCounter - 7);
        }
    }
}