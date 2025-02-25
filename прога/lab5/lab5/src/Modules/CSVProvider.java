package Modules;

import CollectionObject.Coordinates;
import CollectionObject.LocationFrom;
import CollectionObject.LocationTo;
import CollectionObject.Route;
import Exceptions.EmptyFieldException;
import Exceptions.NegativeFieldException;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Класс для работы с CSV-файлами коллекции. Реализует интерфейс DataProvider.
 * Обеспечивает загрузку и сохранение данных в формате CSV.
 */
public class CSVProvider implements DataProvider {

    /** Путь к CSV-файлу с коллекцией */
    protected static Path COLLECTION_PATH;

    /** Максимальный идентификатор элемента в коллекции */
    private long maxId = 0L;

    /** Ссылка на коллекцию маршрутов */
    private Stack<Route> stack = CollectionService.collection;

    /**
     * Конструктор инициализирует путь к файлу коллекции
     * @param collectionPath путь к CSV-файлу коллекции
     */
    public CSVProvider(Path collectionPath) {
        COLLECTION_PATH = collectionPath;
    }

    /**
     * Сохраняет коллекцию в CSV-файл
     * @param collection коллекция для сохранения
     * @throws FileNotFoundException если файл не найден
     * @throws IOException при ошибках ввода/вывода
     */
    @Override
    public void save(Stack<Route> collection) {
        File collectionFile = new File(String.valueOf(COLLECTION_PATH));

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(collectionFile));

            byte[] header = (
                    "id,Название,Координата x,Координата y," +
                            "Координата x из которой стартовали,Координата y из которой стартовали," +
                            "Координата z из которой стартовали,Координата x в которую приехали," +
                            "Координата y в которую приехали,Координата z в которую приехали," +
                            "Дата создания,Пробег\n"
            ).getBytes(StandardCharsets.UTF_8);

            bos.write(header);
            saveToCSV(collection, bos);
            bos.flush();
            bos.close();

            System.out.println("Коллекция успешно сохранена");

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода");
        }
    }

    /**
     * Загружает коллекцию из CSV-файла
     * @throws IOException при ошибках чтения файла
     * @throws CsvException при ошибках парсинга CSV
     * @throws ParseException при ошибках парсинга даты
     * @throws EmptyFieldException при отсутствии обязательных полей
     * @throws NegativeFieldException при отрицательных значениях полей
     */
    @Override
    public void load() {
        File collectionFile = new File(String.valueOf(COLLECTION_PATH));

        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(collectionFile));
            CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
            CSVReader csvReader = new CSVReaderBuilder(fileReader).withCSVParser(parser).withSkipLines(1).build();

            List<String[]> lines = csvReader.readAll();

            try {
                for (String[] line : lines) {
                    Route el = new Route();
                    Coordinates coordinates = new Coordinates();
                    LocationFrom from = new LocationFrom();
                    LocationTo to = new LocationTo();

                    // Парсинг и валидация полей
                    var id = askId(line[0]);
                    if (id > maxId) maxId = id;
                    el.setId(id);
                    CollectionService.elementsCount = maxId;

                    el.setName(askString(line[1]));

                    coordinates.setX(askX(line[2]));
                    coordinates.setY(askYcoordinates(line[3]));
                    el.setCoordinates(coordinates);

                    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                    el.setCreationDate(formatter.parse(line[4]));

                    from.setX(askY(line[4]));
                    from.setY(askY(line[5]));
                    from.setZ(askZ(line[6]));
                    el.setFrom(from);

                    to.setX(askY(line[7]));
                    to.setY(askY(line[8]));
                    to.setZ(askZ(line[9]));
                    el.setTo(to);

                    el.setDistance(askDouble(line[10]));

                    stack.add(el);
                    System.out.println("Элемент " + id + " успешно загружен");
                }
            } catch (NumberFormatException e) {
                handleError("Ошибка формата числа. Проверьте разделитель в файле коллекции");
            } catch (ParseException e) {
                handleError("Ошибка конвертации объекта. Проверьте файл");
            } catch (EmptyFieldException | NegativeFieldException e) {
                handleError(e.getMessage());
            }
        } catch (IOException e) {
            handleError("Ошибка ввода/вывода. Проверьте путь или имя файла");
        } catch (CsvException e) {
            handleError("Невалидный файл коллекции. Проверьте разделители");
        }
    }

    /**
     * Записывает данные коллекции в CSV-формат
     * @param stack коллекция для записи
     * @param bos буферизированный выходной поток
     * @throws IOException при ошибках записи
     */
    private void saveToCSV(Stack<Route> stack, BufferedOutputStream bos) throws IOException {
        for (Route vehicle : stack) {
            String record = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    vehicle.getId(),
                    vehicle.getName(),
                    vehicle.getCoordinates().getX(),
                    vehicle.getCoordinates().getY(),
                    vehicle.getFrom().getX(),
                    vehicle.getFrom().getY(),
                    vehicle.getFrom().getZ(),
                    vehicle.getTo().getX(),
                    vehicle.getTo().getY(),
                    vehicle.getTo().getZ(),
                    vehicle.getCreationDate(),
                    vehicle.getDistance());

            bos.write(record.getBytes(StandardCharsets.UTF_8));
        }
    }

    // Методы валидации ========================================================

    /**
     * Валидирует идентификатор элемента
     * @param num строка с идентификатором
     * @return валидный числовой идентификатор
     * @throws NegativeFieldException если значение ≤ 0
     */
    private Long askId(String num) throws NegativeFieldException {
        long id = Long.parseLong(num);
        if (id <= 0) throw new NegativeFieldException("ID должен быть больше нуля");
        return id;
    }

    /**
     * Валидирует строковое поле
     * @param name входная строка
     * @return непустую строку
     * @throws EmptyFieldException если строка пустая
     */
    private String askString(String name) throws EmptyFieldException {
        if (name.isEmpty()) throw new EmptyFieldException("Обязательное поле отсутствует");
        return name.trim();
    }

    /**
     * Валидирует координату X
     * @param num строка с числовым значением
     * @return значение координаты
     * @throws NumberFormatException при неверном формате числа
     */
    private float askX(String num) throws NumberFormatException {
        return Float.parseFloat(num);
    }

    /**
     * Валидирует координату Y
     * @param num строка с числовым значением
     * @return значение координаты
     * @throws NumberFormatException при неверном формате числа
     */
    private float askY(String num) throws NumberFormatException {
        return Float.parseFloat(num);
    }

    /**
     * Валидирует координату Y (целочисленную)
     * @param num строка с числовым значением
     * @return значение координаты
     * @throws NumberFormatException при неверном формате числа
     */
    private int askYcoordinates(String num) throws NumberFormatException {
        return Integer.parseInt(num);
    }

    /**
     * Валидирует координату Z
     * @param num строка с числовым значением
     * @return значение координаты
     * @throws NumberFormatException при неверном формате числа
     */
    private int askZ(String num) throws NumberFormatException {
        return Integer.parseInt(num);
    }

    /**
     * Валидирует значение дистанции
     * @param n строка с числовым значением
     * @return значение дистанции
     * @throws NumberFormatException при неверном формате числа
     * @throws NegativeFieldException если значение ≤ 0
     */
    private double askDouble(String n) throws NumberFormatException, NegativeFieldException {
        double num = Double.parseDouble(n);
        if (num <= 0) throw new NegativeFieldException("Дистанция должна быть положительной");
        return num;
    }

    /**
     * Обрабатывает критические ошибки загрузки
     * @param message сообщение об ошибке
     */
    private void handleError(String message) {
        System.out.println(message);
        System.exit(1);
    }
}