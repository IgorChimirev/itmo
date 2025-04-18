package Modules;

import CollectionObject.*;
import Exceptions.*;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import Exceptions.EmptyFieldException;
import Exceptions.NegativeFieldException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Locale;


public class CSVProvider implements DataProvider {
    protected static Path COLLECTION_PATH;
    private long maxId = 0L;
    private final HashMap<Long, Route> collection = CollectionService.collection;

    public CSVProvider(Path collectionPath) {
        COLLECTION_PATH = collectionPath;
    }

    @Override
    public void save(HashMap<Long, Route> collection) {
        File collectionFile = new File(String.valueOf(COLLECTION_PATH));

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(collectionFile))) {
            bos.write((
                    "id,name,coord_x,coord_y,from_x,from_y,from_z,to_x,to_y,to_z,creation_date,distance\n"
            ).getBytes(StandardCharsets.UTF_8));

            for (Map.Entry<Long, Route> entry : collection.entrySet()) {
                Route vehicle = entry.getValue();
                try {
                    String record = buildCSVRecord(vehicle);
                    bos.write(record.getBytes(StandardCharsets.UTF_8));
                } catch (NegativeFieldException | EmptyFieldException e) {
                    System.out.println("[Ошибка] Элемент ID " + vehicle.getId() + " не сохранен: " + e.getMessage());
                }
            }
            System.out.println("Коллекция сохранена (" + collection.size() + " элементов)");

        } catch (IOException e) {
            System.out.println("Ошибка сохранения файла: " + e.getMessage());
        }
    }

    private String buildCSVRecord(Route vehicle) throws NegativeFieldException, EmptyFieldException {
        validateVehicle(vehicle);
        return String.format(Locale.US,
                "%d,\"%s\",%.2f,%.2f,%.2f,%.2f,%d,%.2f,%.2f,%d,\"%s\",%.2f%n",
                vehicle.getId(),
                escapeCSV(validateString(vehicle.getName(), "Название")),
                vehicle.getCoordinates().getX(),
                vehicle.getCoordinates().getY(),
                vehicle.getFrom().getX(),
                vehicle.getFrom().getY(),
                vehicle.getFrom().getZ(),
                vehicle.getTo().getX(),
                vehicle.getTo().getY(),
                vehicle.getTo().getZ(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vehicle.getCreationDate()),
                validateDistance(vehicle.getDistance())
        );
    }

    private double validateDistance(double distance) throws NegativeFieldException {
        if (distance <= 0) throw new NegativeFieldException("Дистанция");
        return distance;
    }

    private void validateVehicle(Route vehicle) {
        if (vehicle.getCoordinates() == null ||
                vehicle.getFrom() == null ||
                vehicle.getTo() == null) {
            throw new RuntimeException("Объект содержит null-поля");
        }
    }

    private String escapeCSV(String input) {
        return input.replace("\"", "\"\"");
    }

    @Override
    public void load() {
        File collectionFile = new File(String.valueOf(COLLECTION_PATH));

        try (BufferedReader reader = new BufferedReader(new FileReader(collectionFile))) {
            CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(parser)
                    .withSkipLines(1)
                    .build();

            processCSVLines(csvReader.readAll());

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + COLLECTION_PATH);
        } catch (IOException | CsvException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Критическая ошибка: " + e.getMessage());
        }
    }

    private void processCSVLines(List<String[]> lines) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (String[] line : lines) {
            try {
                if (line.length != 12) {
                    System.out.println("Некорректная строка: " + Arrays.toString(line));
                    continue;
                }

                Route route = parseRoute(line, dateFormat);
                collection.put(route.getId(), route);
                updateMaxId(route.getId());

            } catch (NegativeFieldException e) {
                System.out.println("Ошибка значения: " + e.getMessage());
            } catch (EmptyFieldException e) {
                System.out.println("Отсутствует поле: " + e.getMessage());
            } catch (ParseException e) {
                System.out.println("Ошибка формата даты: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка формата числа: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Неизвестная ошибка: " + e.getMessage());
            }
        }
        CollectionService.elementsCount = maxId;
    }

    private Route parseRoute(String[] line, SimpleDateFormat dateFormat)
            throws ParseException, EmptyFieldException, NegativeFieldException {

        Route route = new Route();
        route.setId(parseLong(line[0]));
        route.setName(validateString(line[1], "Название"));
        route.setCoordinates(parseCoordinates(line));
        route.setFrom(parseLocationFrom(line));
        route.setTo(parseLocationTo(line));
        route.setCreationDate(dateFormat.parse(line[10]));
        route.setDistance(parseDouble(line[11], "Дистанция"));

        return route;
    }

    private long parseLong(String value) throws NegativeFieldException {
        try {
            long id = Long.parseLong(value);
            if (id <= 0) throw new NegativeFieldException("ID");
            return id;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("ID: " + value);
        }
    }

    private Coordinates parseCoordinates(String[] line) {
        Coordinates coord = new Coordinates();
        coord.setX(parseFloat(line[2], "Координата X"));
        coord.setY(parseInt(line[3], "Координата Y"));
        return coord;
    }

    private LocationFrom parseLocationFrom(String[] line) throws NegativeFieldException {
        LocationFrom from = new LocationFrom();
        from.setX(parseDouble(line[4], "From X"));
        from.setY(parseDouble(line[5], "From Y"));
        from.setZ(parseInt(line[6], "From Z"));
        return from;
    }

    private LocationTo parseLocationTo(String[] line) throws NegativeFieldException {
        LocationTo to = new LocationTo();
        to.setX(parseDouble(line[7], "To X"));
        to.setY(parseDouble(line[8], "To Y"));
        to.setZ(parseInt(line[9], "To Z"));
        return to;
    }

    private String validateString(String value, String fieldName) throws EmptyFieldException {
        if (value == null || value.trim().isEmpty()) {
            throw new EmptyFieldException(fieldName);
        }
        return value.trim();
    }

    private float parseFloat(String value, String fieldName) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName + ": " + value);
        }
    }

    private int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName + ": " + value);
        }
    }

    private double parseDouble(String value, String fieldName) throws NegativeFieldException {
        try {
            double num = Double.parseDouble(value);
            if (num <= 0) throw new NegativeFieldException(fieldName);
            return num;
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName + ": " + value);
        }
    }

    private void updateMaxId(long id) {
        if (id > maxId) maxId = id;
    }
}