package Modules;

import CollectionObject.Coordinates;
import CollectionObject.RouteModel;
import Commands.*;
import Network.Request;
import Network.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.*;

public class Server {
    private InetSocketAddress address;
    private Selector selector;
    private ConsoleApp consoleApp;
    private Response response;
    private Request request;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public Server(InetSocketAddress address) {
        this.address = address;
        this.consoleApp = this.createConsoleApp();
        setupLogger();
    }

    private void setupLogger() {
        try {
            LogManager.getLogManager().readConfiguration(
                    Server.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Не удалось загрузить конфигурацию логгера", e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LogManager.getLogManager().reset();
        }));
    }

    public void run(String[] args) {
        try {
            String pathToCollection = args[0];
            CSVProvider csvProvider = new CSVProvider(Path.of(pathToCollection));
            csvProvider.load();
            logger.log(Level.INFO, "Коллекция загружена");

            this.selector = Selector.open();
            logger.log(Level.INFO, "Селектор открыт");

            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(this.address);
            serverChannel.configureBlocking(false);
            logger.log(Level.INFO, "Канал сервера готов к работе");
            serverChannel.register(this.selector, 16);

            while(true) {
                this.selector.select();
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                logger.log(Level.INFO, "Итератор по ключам селектора успешно получен");

                while(keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    logger.log(Level.INFO, "Обработка ключа началась");

                    try {
                        if (key.isValid()) {
                            if (key.isAcceptable()) {
                                ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
                                SocketChannel clientChannel = serverSocketChannel.accept();
                                logger.log(Level.INFO, "Установлено соединение с клиентом " + clientChannel.socket().toString());
                                clientChannel.configureBlocking(false);
                                clientChannel.register(this.selector, 1);
                            }

                            if (key.isReadable()) {
                                SocketChannel clientChannel = (SocketChannel)key.channel();
                                clientChannel.configureBlocking(false);
                                ByteBuffer clientData = ByteBuffer.allocate(2048);
                                int bytesRead = clientChannel.read(clientData);

                                if (bytesRead == -1) {
                                    logger.log(Level.INFO, "Клиент закрыл соединение. Сохраняем коллекцию...");
                                    CommandHandler.save(); // Основное изменение здесь
                                    key.cancel();
                                    clientChannel.close();
                                    continue;
                                }

                                logger.log(Level.INFO, bytesRead + " байт пришло от клиента");

                                try (ObjectInputStream clientDataIn = new ObjectInputStream(
                                        new ByteArrayInputStream(clientData.array()))) {
                                    this.request = (Request)clientDataIn.readObject();
                                } catch (StreamCorruptedException e) {
                                    key.cancel();
                                    continue;
                                }

                                String commandName = this.request.getCommandName();
                                String commandStrArg = this.request.getCommandStrArg();
                                RouteModel commandObjArg = this.request.getCommandObjArg();

                                if (ConsoleApp.commandList.containsKey(commandName)) {
                                    this.response = ConsoleApp.commandList.get(commandName)
                                            .execute(commandStrArg, commandObjArg);
                                    CommandHandler.addCommand(commandName);
                                } else {
                                    this.response = new Response("Команда не найдена. Используйте help для справки", "");
                                }

                                logger.log(Level.INFO, "Запрос:\n" + commandName + "\n" + commandStrArg + "\n"
                                        + commandObjArg + "\nУспешно обработан");
                                clientChannel.register(this.selector, 4);
                            }

                            if (key.isWritable()) {
                                SocketChannel clientChannel = (SocketChannel)key.channel();
                                clientChannel.configureBlocking(false);

                                try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                     ObjectOutputStream clientDataOut = new ObjectOutputStream(bytes)) {

                                    clientDataOut.writeObject(this.response);
                                    ByteBuffer clientData = ByteBuffer.wrap(bytes.toByteArray());
                                    ByteBuffer dataLength = ByteBuffer.allocate(4).putInt(clientData.limit());
                                    dataLength.flip();

                                    clientChannel.write(dataLength);
                                    logger.log(Level.INFO, "Длина ответа (" + clientData.limit() + ") отправлена клиенту");

                                    clientChannel.write(clientData);
                                    logger.log(Level.INFO, "Ответ отправлен клиенту");
                                }

                                clientChannel.register(this.selector, 1);
                            }
                        }
                    } catch (CancelledKeyException | SocketException e) {
                        logger.log(Level.WARNING, "Клиент " + key.channel().toString() + " отключился");
                        CommandHandler.save();
                        key.cancel();
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.log(Level.SEVERE, "Не указан путь к файлу коллекции");
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, "Остановка сервера через консоль");
            CommandHandler.save();
            System.exit(1);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка ввода/вывода");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Несоответствующие классы" + e.getStackTrace());
        }
    }

    private ConsoleApp createConsoleApp() {
        CommandHandler handler = new CommandHandler();
        return new ConsoleApp(
                new HelpCommand(handler),
                new replaceIfLowerCommand(handler),
                new InfoCommand(handler),
                new ShowCommand(handler),
                new AddCommand(handler),
                new UpdateCommand(handler),
                new RemoveLowerCommand(handler),
                new ClearCommand(handler),
                new ExecuteScriptCommand(handler),
                new ExitCommand(handler),
                new PrintFieldDescendingDistance(handler),
                new ReorderCommand(handler),
                new HistoryCommand(handler),
                new InsertNullCommand(handler),
                new RemoveByIdCommand(handler),
                new FilterLessThanDistance(handler),
                new RemoveKeyCommand(handler)
        );
    }
}