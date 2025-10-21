package Modules;

import CollectionObject.*;
import Commands.Command;
import Exceptions.*;
import Network.Response;
import Network.User;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CommandHandler {
    private CollectionService collectionService;
    private static Hashtable<Integer, String> commandHistory = new Hashtable<>();
    private static int commandCounter = 0;
    private static Set<Path> scriptsNames = new HashSet<>(); // Исправлено на HashSet
    private Map<String, Command> commandList; // Добавлено поле для хранения команд

    public CommandHandler() {
        this.collectionService = new CollectionService();
        this.commandList = commandList;
    }

    // ================================= Основные команды =================================
    public synchronized Response help(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Неверные аргументы команды", new Stack<>());
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
        return new Response(helpMessage, new Stack<>());
    }

    public synchronized Response info(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", new Stack<>());
        }
        addCommand("info");
        return new Response(collectionService.info(), convertToStack(collectionService.show()));
    }
    public synchronized Response clear(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", null);
        }
        try {
            collectionService.clear(user);
            addCommand("clear");
            return new Response("Коллекция очищена", null);
        } catch (DBProviderException e) {
            return new Response("Ошибка БД: " + e.getMessage(), null);
        }
    }
    public synchronized Response removeLower(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Требуется ID", null);
        }
        try {
            long id = Long.parseLong(strArgument);
            collectionService.removeLower(id, user);
            addCommand("removeLower " + id);
            return new Response("Элементы удалены", null);
        } catch (NumberFormatException | DBProviderException e) {
            return new Response(e.getMessage(), null);
        }
    }
    public synchronized Response removeKey(User user, String strArgument, RouteModel objArgument) {
        // Проверка аргументов
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: используйте формат 'remove_key <ключ>'", new Stack<>());
        }

        try {
            // Парсинг ключа
            long key = Long.parseLong(strArgument);

            // Удаление элемента
            collectionService.removeByKey(key, user);
            addCommand("remove_key " + key);

            // Получение и преобразование обновленной коллекции
            Stack<Route> updatedCollection = new Stack<>();
            updatedCollection.addAll(collectionService.show());

            return new Response("Элемент с ключом " + key + " удалён", updatedCollection);

        } catch (NumberFormatException e) {
            return new Response("Ошибка формата: ключ должен быть целым числом (пример: remove_key 42)", new Stack<>());
        } catch (NonExistingElementException e) {
            return new Response("Ошибка: элемент с ключом " + strArgument + " не найден", new Stack<>());
        } catch (DBProviderException e) {
            return new Response("Ошибка базы данных: " + e.getMessage(), new Stack<>());
        } catch (Exception e) {
            return new Response("Неизвестная ошибка: " + e.getMessage(), new Stack<>());
        }
    }
    public synchronized Response replaceIfLower(User user, String strArgument, RouteModel objArgument) {
        // Проверка наличия аргументов
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: используйте формат 'replaceIfLower <key> <distance>'", new Stack<>());
        }

        // Разбивка аргументов
        String[] args = strArgument.split(" ");
        if (args.length != 2) {
            return new Response("Ошибка: требуется 2 аргумента (ключ и дистанция)", new Stack<>());
        }

        try {
            // Парсинг аргументов
            long key = Long.parseLong(args[0]);
            double distance = Double.parseDouble(args[1]);

            // Выполнение операции
            collectionService.replaceIfLower(key, distance, user);
            addCommand("replaceIfLower " + key);

            // Получение и преобразование коллекции
            Stack<Route> updatedCollection = new Stack<>();
            updatedCollection.addAll(collectionService.show());

            return new Response("Элемент с ключом " + key + " обновлён", updatedCollection);

        } catch (NumberFormatException e) {
            return new Response("Ошибка формата: ключ должен быть целым числом, дистанция - дробным", new Stack<>());
        } catch (DBProviderException e) {
            return new Response("Ошибка базы данных: " + e.getMessage(), new Stack<>());

        } catch (Exception e) {
            return new Response("Неизвестная ошибка: " + e.getMessage(), new Stack<>());
        }
    }
    public synchronized Response filterStartsWithName(User user, String strArgument, RouteModel objArgument) {
        // Проверка аргументов
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: требуется префикс (например: filterStartsWithName abc)", new Stack<>());
        }

        try {
            // Получение и преобразование результата
            List<Route> result = collectionService.filterStartsWithName(strArgument);
            Stack<Route> resultStack = new Stack<>();
            resultStack.addAll(result);

            // Логирование команды
            addCommand("filterStartsWithName " + strArgument);

            // Формирование ответа
            return resultStack.isEmpty()
                    ? new Response("Элементы с именем, начинающимся на '" + strArgument + "' не найдены", new Stack<>())
                    : new Response("Найдено " + result.size() + " элементов:", resultStack);

        } catch (NonExistingElementException e) {
            return new Response("Ошибка поиска: " + e.getMessage(), new Stack<>());
        } catch (Exception e) {
            return new Response("Неизвестная ошибка: " + e.getMessage(), new Stack<>());
        }
    }
    public synchronized Response update(User user, String strArgument, RouteModel objArgument) {
        // Проверка аргументов
        if (strArgument.isEmpty() || objArgument == null) {
            return new Response("Ошибка: используйте формат 'update <ID> {объект}'", new Stack<>());
        }

        try {
            // Парсинг ID
            long id = Long.parseLong(strArgument);

            // Обновление элемента
            collectionService.update(id, objArgument, user);
            addCommand("update " + id);

            // Получение и преобразование обновленной коллекции
            Stack<Route> updatedCollection = new Stack<>();
            updatedCollection.addAll(collectionService.show());

            return new Response("Элемент с ID " + id + " обновлён", updatedCollection);

        } catch (NumberFormatException e) {
            return new Response("Ошибка формата: ID должен быть целым числом", new Stack<>());
        } catch (NonExistingElementException e) {
            return new Response("Ошибка: элемент с ID " + strArgument + " не найден", new Stack<>());
        } catch (DBProviderException e) {
            return new Response("Ошибка базы данных: " + e.getMessage(), new Stack<>());
        } catch (Exception e) {
            return new Response("Неизвестная ошибка: " + e.getMessage(), new Stack<>());
        }
    }

    public synchronized Response reorder(User user, String strArgument, RouteModel objArgument) {
        // Проверка аргументов
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: команда не принимает аргументов", new Stack<>());
        }

        addCommand("reorder");

        // Получение и преобразование отсортированной коллекции
        Stack<Route> sortedCollection = new Stack<>();
        sortedCollection.addAll(collectionService.reorder());

        return new Response("Коллекция отсортирована в обратном порядке", sortedCollection);
    }

    public synchronized Response clearAll(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: команда не требует аргументов", new Stack<>());
        }
        if (!user.getUsername().equals("admin")) {
            return new Response("Ошибка: недостаточно прав", new Stack<>());
        }
        try {
            collectionService.clearAll();
            addCommand("clear_all");
            return new Response("Коллекция полностью очищена", new Stack<>());
        } catch (DBProviderException e) {
            return new Response("Ошибка БД: " + e.getMessage(), new Stack<>());
        }
    }
    public synchronized Response filter_less_than_distance(User user, String strArgument, RouteModel objArgument) {
        // Проверка аргументов
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: используйте формат 'filter_less_than_distance <дистанция>'", new Stack<>());
        }

        try {
            // Парсинг дистанции
            double distance = Double.parseDouble(strArgument);

            // Получение и преобразование результата
            List<Route> result = collectionService.filter_less_than_distance(distance);
            Stack<Route> resultStack = new Stack<>();
            resultStack.addAll(result);

            // Логирование команды
            addCommand("filter_less_than_distance " + distance);

            // Формирование ответа
            return resultStack.isEmpty()
                    ? new Response("Элементы с дистанцией меньше " + distance + " не найдены", new Stack<>())
                    : new Response("Найдено " + resultStack.size() + " элементов:", resultStack);

        } catch (NumberFormatException e) {
            return new Response("Ошибка формата: дистанция должна быть числом (например: 12.5)", new Stack<>());
        } catch (Exception e) {
            return new Response("Неизвестная ошибка: " + e.getMessage(), new Stack<>());
        }
    }

    public synchronized Response showUserElements(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Используйте: show_user_elements <username>", new Stack<>());
        }

        try {
            List<Route> userRoutes = collectionService.filterByUsername(strArgument);
            addCommand("show_user_elements " + strArgument);
            return userRoutes.isEmpty()
                    ? new Response("У пользователя " + strArgument + " нет элементов", new Stack<>())
                    : new Response("Элементы пользователя " + strArgument + ":", convertToStack(userRoutes));
        } catch (Exception e) {
            return new Response("Ошибка: " + e.getMessage(), new Stack<>());
        }
    }
    public synchronized Response removeById(User user, String strArgument, RouteModel objArgument) {
        // Проверка аргументов
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: используйте формат 'removeById <ID>'", new Stack<>());
        }

        try {
            // Парсинг ID
            long id = Long.parseLong(strArgument);

            // Удаление элемента
            collectionService.removeById(id, user);
            addCommand("removeById " + id);

            // Получение и преобразование обновленной коллекции
            Stack<Route> updatedCollection = new Stack<>();
            updatedCollection.addAll(collectionService.show());

            return new Response("Элемент с ID " + id + " успешно удалён", updatedCollection);

        } catch (NumberFormatException e) {
            return new Response("Ошибка формата: ID должен быть целым числом", new Stack<>());
        } catch (NonExistingElementException e) {
            return new Response("Ошибка: элемент с ID " + strArgument + " не найден", new Stack<>());
        } catch (DBProviderException e) {
            return new Response("Ошибка базы данных: " + e.getMessage(), new Stack<>());
        } catch (Exception e) {
            return new Response("Неизвестная ошибка: " + e.getMessage(), new Stack<>());
        }
    }
    public synchronized Response add_random(User user, String strArgument, RouteModel objArgument) {
        // Проверка аргументов
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: команда не принимает аргументов (пример: add_random)", new Stack<>());
        }

        try {
            // Добавление элементов
            collectionService.addRandomElements(user);
            addCommand("add_random");

            // Получение и преобразование коллекции
            Stack<Route> updatedCollection = new Stack<>();
            updatedCollection.addAll(collectionService.show());

            return new Response("Успешно добавлено 1000 случайных элементов", updatedCollection);

        } catch (DBProviderException e) {
            return new Response("Ошибка базы данных: " + e.getMessage(), new Stack<>());
        } catch (Exception e) {
            return new Response("Неизвестная ошибка: " + e.getMessage(), new Stack<>());
        }
    }
    public synchronized Response add(User user, String strArgument, RouteModel objArgument) {
        // Проверка аргументов
        if (!strArgument.isEmpty() || objArgument == null) {
            return new Response("Ошибка: используйте формат 'add' (объект передается отдельно)", new Stack<>());
        }

        try {
            // Добавление элемента
            collectionService.add(objArgument, user);
            addCommand("add");

            // Получение и преобразование коллекции
            Stack<Route> updatedCollection = new Stack<>();
            updatedCollection.addAll(collectionService.show());

            return new Response("Элемент успешно добавлен", updatedCollection);

        } catch (DBProviderException e) {
            return new Response("Ошибка базы данных: " + e.getMessage(), new Stack<>());
        } catch (Exception e) {
            return new Response("Неизвестная ошибка: " + e.getMessage(), new Stack<>());
        }
    }
    public synchronized Response show(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", new Stack<>());
        }
        addCommand("show");
        Stack<Route> collectionStack = convertToStack(collectionService.show());
        return collectionStack.isEmpty()
                ? new Response("Коллекция пуста", new Stack<>())
                : new Response("Содержимое коллекции:", collectionStack);
    }

    public synchronized Response filterByUser(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: команда не требует аргументов", new Stack<>());
        }
        try {
            List<Route> userRoutes = collectionService.filterByUser(user.getUsername());
            addCommand("filter_by_user");
            return userRoutes.isEmpty()
                    ? new Response("У вас нет элементов в коллекции", new Stack<>())
                    : new Response("Ваши элементы:", convertToStack(userRoutes));
        } catch (Exception e) {
            return new Response("Ошибка: " + e.getMessage(), new Stack<>());
        }
    }

    // ============================== Методы модификации ==============================
    public synchronized Response insertNull(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument == null) {
            return new Response("Требуются ключ и объект", new Stack<>());
        }

        try {
            long key = Long.parseLong(strArgument);
            collectionService.insertElement(key, objArgument, user);
            addCommand("insertNull " + key);
            return new Response("Элемент добавлен", convertToStack(collectionService.show()));
        } catch (NumberFormatException e) {
            return new Response("Неверный формат ключа", new Stack<>());
        } catch (NonExistingElementException | DBProviderException e) {
            return new Response(e.getMessage(), new Stack<>());
        }
    }

    // Аналогичные исправления для всех остальных методов...

    // ============================== Работа с скриптами ==============================
    public synchronized Response executeScript(User user, String strArgument, RouteModel objArgument) {
        if (strArgument.isEmpty() || objArgument != null) {
            return new Response("Используйте: execute_script <file_path>", new Stack<>());
        }

        try {
            Path path = Paths.get(strArgument).toAbsolutePath().normalize();

            if (scriptsNames.contains(path)) {
                throw new ScriptRecursionException("Рекурсивный вызов запрещён");
            }

            scriptsNames.add(path);

            try (Scanner scanner = new Scanner(path)) {
                StringBuilder output = new StringBuilder();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split(" ", 2);
                    String command = parts[0];
                    String args = parts.length > 1 ? parts[1] : "";

                    Command cmd = commandList.get(command.toLowerCase());
                    if (cmd == null) {
                        output.append("Неизвестная команда: ").append(command).append("\n");
                        continue;
                    }

                    Response response = cmd.execute(user, args, null);
                    output.append(response.getMessage()).append("\n");
                }
                return new Response("Скрипт выполнен:\n" + output, new Stack<>());
            } finally {
                scriptsNames.remove(path);
            }
        } catch (FileNotFoundException e) {
            return new Response("Файл не найден: " + strArgument, new Stack<>());
        } catch (ScriptRecursionException e) {
            return new Response(e.getMessage(), new Stack<>());
        } catch (Exception e) {
            return new Response("Ошибка выполнения: " + e.getMessage(), new Stack<>());
        }
    }

    // ============================== Вспомогательные методы ==============================
    private Stack<Route> convertToStack(List<Route> list) {
        Stack<Route> stack = new Stack<>();
        stack.addAll(list);
        return stack;
    }
    public synchronized Response history(User user, String strArgument, RouteModel objArgument) {
        if (!strArgument.isEmpty() || objArgument != null) {
            return new Response("Ошибка: команда не принимает аргументов", new Stack<>());
        }

        addCommand("history");
        StringBuilder history = new StringBuilder("Последние 7 команд:\n");
        commandHistory.values().forEach(cmd -> history.append(cmd).append("\n"));

        return new Response(history.toString(), new Stack<>());
    }


    public static synchronized void addCommand(String command) {
        commandCounter++;
        commandHistory.put(commandCounter, command);
        if (commandHistory.size() > 7) {
            commandHistory.remove(commandCounter - 7);
        }
    }
}