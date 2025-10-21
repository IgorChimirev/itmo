import CollectionObject.RouteModel;
import Exceptions.ScriptRecursionException;
import Network.Request;
import Network.Response;
import Network.User;
import Utils.PasswordHasher;
import Utils.PromptScan;
import Utils.VehicleAsker;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Client {
    private Set<Path> scriptsNames = new TreeSet<>();
    private InetAddress host;
    private int port;
    private SocketChannel channel;
    private User user;

    public Client(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        try {
            SocketAddress address = new InetSocketAddress(host, port);
            channel = SocketChannel.open();
            channel.connect(address);

            PromptScan.setUserScanner(new Scanner(System.in));
            var scanner = PromptScan.getUserScanner();

            System.out.println("Это крутое консольное приложение запущенно специально для пацанов");
            authenticateUser();

            while (true) {
                System.out.print("> ");
                try {
                    do {
                        var command = "";
                        var arguments = "";
                        String[] input = (scanner.nextLine() + " ").trim().split(" ", 2);
                        if (input.length == 2) {
                            arguments = input[1].trim();
                        }
                        command = input[0].trim();

                        processUserPrompt(command, arguments);
                        System.out.print("> ");
                    } while (scanner.hasNext());

                } catch (NoSuchElementException e) {
                    System.out.println("Остановка клиента через консоль");
                    System.exit(1);
                } catch (ClassNotFoundException e) {
                    System.out.println("Объект поступивший в ответ от сервера не найден");
                } catch (SocketException e) {
                    System.out.println("Сервер был остановлен во время обработки вашего запроса. Пожалуйста, повторите попытку позже");
                    System.exit(1);
                }
            }

        } catch (ConnectException e) {
            System.out.println("Сервер недоступен в данный момент. Пожалуйста, повторите попытку позже");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка ввода/вывода");
        }
    }

    private void authenticateUser() {
        var scanner = PromptScan.getUserScanner();
        var username = "";
        var password = "";

        try {
            while (true) {
                System.out.println("Введите логин: ");
                username = scanner.nextLine();

                System.out.println("Введите пароль: ");
                password = scanner.nextLine();

                user = new User(username, PasswordHasher.getHash(password));

                Request userAuthenticationRequest = new Request(user, false);
                Response response = sendAndReceive(userAuthenticationRequest);

                if (response.isUserAuthenticated()) {
                    printResponse(response);
                    break;
                } else {
                    printResponse(response);

                    if (response.getMessage().equals("Пользователя " + user.getUsername() + " не существует")) {
                        System.out.println("Если вы хотите зарегистрироваться, нажмите 'y'");
                        String ans = scanner.nextLine().trim();

                        if (ans.equalsIgnoreCase("y")) {
                            while (!registerUser()) {
                                registerUser();
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchElementException e) {
            System.out.println("Ошибка аутентификации: " + e.getMessage());
            System.exit(1);
        }
    }

    private boolean registerUser() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        try {
            var scanner = PromptScan.getUserScanner();

            System.out.println("Введите логин: ");
            String username = scanner.nextLine();

            System.out.println("Введите пароль: ");
            String password = scanner.nextLine();

            User newUser = new User(username, PasswordHasher.getHash(password));

            Request registrationRequest = new Request(newUser, true); // Флаг регистрации
            Response response = sendAndReceive(registrationRequest);

            printResponse(response);

            if (response.isUserAuthenticated()) {
                this.user = newUser; // Обновляем текущего пользователя
                return true;
            }
            return false;

        } catch (NoSuchElementException e) {
            System.out.println("Остановка клиента через консоль");
            System.exit(1);
            return false;
        }
    }

    private void processUserPrompt(String command, String arguments) throws IOException, ClassNotFoundException {
        Request request;
        Response response;
        if (command.equalsIgnoreCase("add") || command.equalsIgnoreCase("update")) {
            RouteModel objArgument = VehicleAsker.createElement(user);
            request = new Request(user, command, arguments, objArgument);
            response = sendAndReceive(request);
            printResponse(response);
        } else if (command.equalsIgnoreCase("exit")) {
            System.out.println("Работа клиентского приложения завершена");
            System.exit(0);
        } else if (command.equalsIgnoreCase("executeScript")) {
            executeScript(arguments);
        } else if (command.equalsIgnoreCase("add_random")) {
        request = new Request(user, command, arguments);
        response = sendAndReceive(request);
        printResponse(response);
    }
        else if (command.equalsIgnoreCase("filter_by_user")) {
            request = new Request(user, command, arguments);
            response = sendAndReceive(request);
            printResponse(response);
        } else if (command.equalsIgnoreCase("show_user_elements")) {
            request = new Request(user, command, arguments);
            response = sendAndReceive(request);
            printResponse(response);
        }
        else if (command.equalsIgnoreCase("clear_all")) {
            request = new Request(user, command, arguments);
            response = sendAndReceive(request);
            printResponse(response);
        }else {
            request = new Request(user, command, arguments);
            response = sendAndReceive(request);
            printResponse(response);
        }
    }

    private Response sendAndReceive(Request request) throws IOException, ClassNotFoundException {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bytes)) {

            out.writeObject(request);
            ByteBuffer dataToSend = ByteBuffer.wrap(bytes.toByteArray());
            channel.write(dataToSend);
            out.flush();
        }

        ByteBuffer dataToReceiveLength = ByteBuffer.allocate(8);
        channel.read(dataToReceiveLength);
        dataToReceiveLength.flip();
        int responseLength = dataToReceiveLength.getInt();

        ByteBuffer responseBytes = ByteBuffer.allocate(responseLength);
        ByteBuffer packetFromServer = ByteBuffer.allocate(256);

        while (true) {
            channel.read(packetFromServer);
            if (packetFromServer.position() == 2 && packetFromServer.get(0) == 28 && packetFromServer.get(1) == 28) break;
            packetFromServer.flip();
            responseBytes.put(packetFromServer);
            packetFromServer.clear();
        }

        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(responseBytes.array()))) {
            return (Response) in.readObject();
        }
    }

    private void printResponse(Response response) {
        System.out.println(response.getMessage());
        try {
            String collection = response.getCollection();
            if (collection != null && !collection.isEmpty()) {
                System.out.println(collection);
            }
        } catch (NullPointerException ignored) {
        }
    }

    private void executeScript(String path) throws ClassNotFoundException {
        if (path.isBlank()) {
            System.out.println("Неверные аргументы команды");
            return;
        }

        try {
            Path pathToScript = Paths.get(path);
            PromptScan.setUserScanner(new Scanner(pathToScript));
            Scanner scriptScanner = PromptScan.getUserScanner();
            Path scriptFile = pathToScript.getFileName();

            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            scriptsNames.add(scriptFile);

            do {
                String[] input = (scriptScanner.nextLine() + " ").trim().split(" ", 2);
                String command = input[0].trim();
                String arguments = input.length > 1 ? input[1].trim() : "";

                while (scriptScanner.hasNextLine() && command.isEmpty()) {
                    input = (scriptScanner.nextLine() + " ").trim().split(" ", 2);
                    command = input[0].trim();
                    arguments = input.length > 1 ? input[1].trim() : "";
                }

                if (command.equalsIgnoreCase("executeScript")) {
                    try {
                        Path scriptNameFromArgument = Paths.get(arguments).getFileName();
                        if (scriptsNames.contains(scriptNameFromArgument)) {
                            throw new ScriptRecursionException("Один и тот же скрипт не может выполнятся рекурсивно");
                        }
                        executeScript(arguments);
                    } catch (ScriptRecursionException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    processUserPrompt(command, arguments);
                }

            } while (scriptScanner.hasNextLine());

            scriptsNames.remove(scriptFile);
            PromptScan.setUserScanner(new Scanner(System.in));
            System.out.println("Скрипт " + scriptFile + " успешно выполнен");

        } catch (FileNotFoundException e) {
            System.out.println("Файл " + path + " не найден");
        } catch (NoSuchElementException e) {
            System.out.println("Файл " + path + " пуст");
        } catch (InvalidPathException | SecurityException | IOException e) {
            System.out.println("Ошибка при обработке скрипта: " + e.getMessage());
        }
    }
}