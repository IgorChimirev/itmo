package Modules;

import CollectionObject.Route;
import CollectionObject.RouteModel;
import Exceptions.NonExistingElementException;
import Exceptions.ScriptRecursionException;
import Network.Response;
import java.io.FileNotFoundException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

public class CommandHandler {
    private CollectionService collectionService;
    private static CSVProvider csvProvider;
    private static Hashtable<Integer, String> commandHistory = new Hashtable<>();
    private static int commandCounter = 0;
    private static Set<Path> scriptsNames = new TreeSet<>();

    public CommandHandler() {
        this.collectionService = new CollectionService();
        csvProvider = new CSVProvider(CSVProvider.COLLECTION_PATH);
    }

    public Response help(String strArgument, RouteModel objArgument) {
        if (!strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }

        String commands = """
            ======= Доступные команды =======
            help - справка
            info - информация о коллекции
            show - показать элементы
            add - добавить элемент
            insertNull <key> - добавить по ключу
            remove_key <key> - удалить по ключу
            update <id> - обновить элемент
            removeById <id> - удалить по ID
            clear - очистить коллекцию
            save - сохранить коллекцию
            executeScript <path> - выполнить скрипт
            exit - выход без сохранения
            replaceIfLower <key> <distance> - заменить если меньше
            removeLower <id> - удалить меньшие элементы
            reorder - обратная сортировка
            history - история команд
            filter_less_than_distance <distance> - фильтр по дистанции
            filterStartsWithName <prefix> - фильтр по имени
            =================================""";
        return new Response(commands, "");
    }

    public Response info(String strArgument, RouteModel objArgument) {
        if (!strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        return new Response(collectionService.info(), collectionService.show().toString());
    }

    public Response show(String strArgument, RouteModel objArgument) {
        if (!strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        return collectionService.show().isEmpty()
                ? new Response("Коллекция пуста", "")
                : new Response("Коллекция:", collectionService.show().toString());
    }

    public Response insertNull(String strArgument, RouteModel objArgument) {
        // Исправлено условие: проверяем, что объект ПЕРЕДАН (objArgument != null -> objArgument == null)
        if (strArgument.isBlank() || objArgument == null) {
            return new Response("Ошибка: требуются ключ и объект", "");
        }

        try {
            long key = Long.parseLong(strArgument);
            collectionService.insertElement(key, objArgument);
            return new Response("Элемент добавлен", collectionService.show().toString());
        } catch (NumberFormatException e) {
            return new Response("Неверный формат ключа", "");
        } catch (NonExistingElementException e) {
            return new Response("Элемент с ключом " + strArgument + " уже существует", "");
        }
    }

    public Response removeKey(String strArgument, RouteModel objArgument) {
        // Fix validation logic: check for missing key OR unexpected object argument
        if (strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: требуется ключ (и отсутствие дополнительных аргументов)", "");
        }

        try {
            long key = Long.parseLong(strArgument);
            collectionService.removeByKey(key); // May throw NonExistingElementException
            return new Response("Элемент удалён", collectionService.show().toString());
        } catch (NumberFormatException e) {
            return new Response("Неверный формат ключа", "");
        } catch (NonExistingElementException e) { // Add this block
            return new Response("Элемент с ключом " + strArgument + " не найден", "");
        }
    }

    public Response add(String strArgument, Route objArgument) {
        if (!strArgument.isBlank() || objArgument == null) {
            return new Response("Ошибка: требуется объект", "");
        }
        collectionService.add(objArgument);
        return new Response("Элемент добавлен", collectionService.show().toString());
    }

    public Response update(String strArgument, RouteModel objArgument) {
        if (strArgument.isBlank() || objArgument == null) {
            return new Response("Ошибка: требуется ID и объект", "");
        }
        try {
            long id = Long.parseLong(strArgument);
            collectionService.update(id, objArgument);
            return new Response("Элемент обновлён", collectionService.show().toString());
        } catch (NumberFormatException | NonExistingElementException e) {
            return new Response(e.getMessage(), "");
        }
    }

    public Response removeById(String strArgument, RouteModel objArgument) {
        if (strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: требуется ID", "");
        }
        try {
            long id = Long.parseLong(strArgument);
            collectionService.removeById(id);
            return new Response("Элемент удалён", collectionService.show().toString());
        } catch (NumberFormatException | NonExistingElementException e) {
            return new Response(e.getMessage(), "");
        }
    }

    public Response clear(String strArgument, RouteModel objArgument) {
        if (!strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        collectionService.clear();
        return new Response("Коллекция очищена", "");
    }
    public static void save(){
        csvProvider.save(CollectionService.collection);
    }

    public Response save(String strArgument, RouteModel objArgument) {
        if (!strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        csvProvider.save(CollectionService.collection);
        return new Response("Коллекция сохранена", "");
    }

    public Response executeScript(String strArgument, RouteModel objArgument) {
        if (strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: Используйте команду как execute_script <file_path>", "");
        }

        try {
            Path path = Paths.get(strArgument);
            Path scriptFile = path.getFileName();

            if (scriptsNames.contains(scriptFile)) {
                throw new ScriptRecursionException("Рекурсивный вызов скрипта запрещен");
            }

            scriptsNames.add(scriptFile);

            try (Scanner scriptScanner = new Scanner(path)) {
                StringBuilder output = new StringBuilder();

                while (scriptScanner.hasNextLine()) {
                    String line = scriptScanner.nextLine().trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split(" ", 2);
                    String command = parts[0].trim();
                    String args = (parts.length > 1) ? parts[1].trim() : "";

                    // Обработка вложенных скриптов
                    if (command.equalsIgnoreCase("execute_script")) {
                        Path nestedScript = Paths.get(args).getFileName();
                        if (scriptsNames.contains(nestedScript)) {
                            output.append("Ошибка: Рекурсия запрещена\n");
                            continue;
                        }
                        Response response = executeScript(args, null);
                        output.append(response.getMessage()).append("\n");
                        continue;
                    }

                    // Вызов других команд
                    if (ConsoleApp.commandList.containsKey(command)) {
                        Response response = ConsoleApp.commandList.get(command).execute(args, null);
                        output.append(response.getMessage()).append("\n");
                    } else {
                        output.append("Неизвестная команда: ").append(command).append("\n");
                    }
                }

                return new Response("Скрипт выполнен:\n" + output, "");
            } finally {
                scriptsNames.remove(scriptFile);
            }

        } catch (FileNotFoundException e) {
            return new Response("Файл не найден: " + strArgument, "");
        } catch (ScriptRecursionException e) {
            return new Response(e.getMessage(), "");
        } catch (InvalidPathException e) {
            return new Response("Некорректный путь", "");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            return new Response("Ошибка выполнения скрипта", "");
        }
    }

    public Response exit(String strArgument, RouteModel objArgument) {
        if (!strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        return new Response("""
            Выйти без сохранения? (y/n)
            """, "");
    }

    public Response replaceIfLower(String strArgument, RouteModel objArgument) {
        if (strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: требуется ключ и значение", "");
        }
        String[] args = strArgument.split(" ");
        if (args.length != 2) {
            return new Response("Ошибка: неверный формат аргументов", "");
        }
        try {
            long key = Long.parseLong(args[0]);
            double distance = Double.parseDouble(args[1]);
            collectionService.replaceIfLower(key, distance);
            return new Response("Элемент обновлён", collectionService.show().toString());
        } catch (NumberFormatException e) {
            return new Response("Неверный формат данных", "");
        }
    }

    public Response removeLower(String strArgument, RouteModel objArgument) {
        if (strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: требуется ID", "");
        }
        try {
            long id = Long.parseLong(strArgument);
            collectionService.removeLower(id);
            return new Response("Элементы удалены", collectionService.show().toString());
        } catch (NumberFormatException e) {
            return new Response("Неверный формат ID", "");
        }
    }

    public Response reorder(String strArgument, RouteModel objArgument) {
        if (!strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        collectionService.reorder();
        return new Response("Коллекция отсортирована", collectionService.show().toString());
    }

    public Response history(String strArgument, RouteModel objArgument) {
        if (!strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: неверные аргументы", "");
        }
        StringBuilder history = new StringBuilder("История:\n");
        commandHistory.values().forEach(cmd -> history.append(cmd).append("\n"));
        return new Response(history.toString(), "");
    }

    public Response filter_less_than_distance(String strArgument, RouteModel objArgument) {
        if (strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: требуется дистанция", "");
        }
        try {
            double distance = Double.parseDouble(strArgument);
            collectionService.filter_less_than_distance(distance);
            return new Response("Результаты фильтрации:", collectionService.show().toString());
        } catch (NumberFormatException e) {
            return new Response("Неверный формат дистанции", "");
        }
    }

    public Response filterStartsWithName(String strArgument, RouteModel objArgument) {
        if (strArgument.isBlank() || objArgument != null) {
            return new Response("Ошибка: требуется префикс", "");
        }
        try {
            var result = collectionService.filterStartsWithName(strArgument);
            return result.isEmpty()
                    ? new Response("Элементы не найдены", "")
                    : new Response("Результаты:", result.toString());
        } catch (NonExistingElementException e) {
            return new Response(e.getMessage(), "");
        }
    }

    public static void addCommand(String command) {
        commandCounter++;
        commandHistory.put(commandCounter, command);
        if (commandHistory.size() > 7) {
            commandHistory.remove(commandCounter - 7);
        }
    }
}