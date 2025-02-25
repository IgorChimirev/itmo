package Modules;

import CollectionObject.*;

import Exceptions.*;

import java.util.*;

import static CollectionObject.Route.*;


public class CollectionService {
    protected static Long elementsCount = 0L;
    private Date initializationDate;
    protected static Stack<Route> collection;
    private boolean isReversed = false;

    protected static Scanner InputScanner;

    public CollectionService() {
        collection = new Stack<>();
        this.initializationDate = new Date();
    }

    private class CompareRoute implements Comparator<Route>{

        @Override
        public int compare(Route o1, Route o2) {
            return o1.getName().length() - o2.getName().length();
        }

        @Override
        public Comparator<Route> reversed() {
            return Comparator.super.reversed();
        }
    }

    protected record RouteWithoutId (
            String name, Coordinates coordinates, Date creationDate, LocationFrom from,
            LocationTo To, double distance){}

    public void addElement(){
        RouteWithoutId source = createElement();

        elementsCount+=1;
        Route newElement = new Route(
                elementsCount,
                source.name,
                source.coordinates,
                source.creationDate,
                source.from,
                source.To,
                source.distance

        );

        collection.addElement(newElement);
        System.out.println("Элемент успешно добавлен");
    }

    public void info(){
        System.out.println("Тип коллекции: " + collection.getClass());
        System.out.println("Дата создания: " + initializationDate);
        System.out.println("Количество элементов: " + collection.size());
    }

    public void show(){
        if (collection.isEmpty()){
            System.out.println("В коллекции пока нету ни одного элемента");
        } else{
            for (Route vehicle: collection) {
                System.out.println(vehicle + "\n");
            }
        }
    }


    public void update(long current_id){
        if (!collection.contains(collection.get((int) current_id))){
            System.out.println("Элемента с таким id не существует");
        }
        for (Route vehicle:collection) {
            if (current_id == vehicle.getId()){
                collection.remove(vehicle);

                RouteWithoutId source = createElement();
                Route newElement = new Route(
                        current_id,
                        source.name,
                        source.coordinates,
                        source.creationDate,
                        source.from,
                        source.To,
                        source.distance

                );

                collection.addElement(newElement);
                System.out.println("Элемент с id " + current_id + " успешно изменён");
                break;

            }
        }
    }

    public void removeById(long id){
        if (!collection.contains(collection.get((int) id))){
            System.out.println("Элемента с таким id не существует");
        }
        for (Route vehicle:collection) {
            if (id == vehicle.getId()){
                collection.remove(vehicle);
                System.out.println("Элемент с id " + id + " успешно удалён");
                break;
            }
        }
    }

    public void clear(){
        collection.clear();
        System.out.println("Все элементы успешно удалены");
    }

    public void removeLower(long startId) {
        long currentId = 1; // начинаем с id = 1

        // Если startId не больше первого id, то нечего удалять
        if (startId <= currentId) {
            System.out.println("Элемента с таким id не существует");
            return;
        }

        // Удаляем элементы, пока id текущего элемента меньше startId.
        // При этом предполагается, что порядок удаления соответствует нарастанию id.
        while (currentId < startId && !collection.isEmpty()) {
            collection.pop();
            System.out.println("Элемент с id " + currentId + " успешно удалён");
            currentId++;
        }
    }





    public void reorder(){
        CompareRoute comparator = new CompareRoute();
        if (!isReversed){
            collection.sort(comparator.reversed());
            isReversed = true;
            System.out.println("///Коллекция отсортирована по убыванию/// \n");
        } else {
            collection.sort(comparator);
            System.out.println("///Коллекция отсортирована по возрастанию/// \n");
        }
        show();
    }


    public void filter_less_than_distance(double Distance){
        int counter = 0;
        for (Route vehicle : collection) {
            if (vehicle.getDistance() < Distance){
                System.out.println(vehicle + "\n");
            }
        }
    }

    public void filterStartsWithName(Double id) {
        // Создаем список для хранения значений distance
        List<Double> distances = new ArrayList<>();

        // Заполняем список значениями distance из коллекции
        for (Route vehicle : collection) {
            distances.add(vehicle.getDistance());
        }

        // Сортируем список в порядке убывания
        distances.sort(Collections.reverseOrder());

        // Печатаем отсортированные значения distance
        for (Double distance : distances) {
            System.out.println(distance);
        }
    }


    private String askString(Scanner InputScanner) {
        while(true) {
            try {
                var name = InputScanner.nextLine();
                if (name.isBlank()){
                    throw new EmptyFieldException("Поле не может быть пустым. Введите его ещё раз: ");
                }
                return name.trim();
            }catch(EmptyFieldException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private float askX(Scanner InputScanner) {
        while(true) {
            try {
                return Float.parseFloat(InputScanner.nextLine());
            }catch (NumberFormatException e){
                System.out.println("Неверный формат числа. Введите его повторно:");
            }
        }
    }

    private int askY(Scanner InputScanner) {
        while(true) {
            try {
                return Integer.parseInt(InputScanner.nextLine());
            }catch (NumberFormatException e){
                System.out.println("Неверный формат числа. Введите его повторно:");
            }
        }
    }

    private float askFloat(Scanner InputScanner) {
        while(true) {
            try {
                float num = Float.parseFloat(InputScanner.nextLine());
                if (num > 0){
                    return num;
                } else {
                    throw new NegativeFieldException("Число не может быть отрицательным. Введите его ещё раз:");
                }

            } catch (NumberFormatException e){
                System.out.println("Неверный формат числа. Введите его повторно:");
            } catch (NegativeFieldException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private double askDouble(Scanner InputScanner) {
        while(true) {
            try {
                double num = Double.parseDouble(InputScanner.nextLine());
                if (num > 0){
                    return num;
                } else {
                    throw new NegativeFieldException("Число не может быть отрицательным. Введите его ещё раз:");
                }
            } catch (NumberFormatException e){
                System.out.println("Неверный формат числа. Введите его повторно:");
            } catch (NegativeFieldException e){
                System.out.println(e.getMessage());
            }
        }
    }
    private int askInt(Scanner InputScanner) {
        while(true) {
            try {
                int num = Integer.parseInt(InputScanner.nextLine());
                if (num > 0){
                    return num;
                } else {
                    throw new NegativeFieldException("Число не может быть отрицательным. Введите его ещё раз:");
                }
            } catch (NumberFormatException e){
                System.out.println("Неверный формат числа. Введите его повторно:");
            } catch (NegativeFieldException e){
                System.out.println(e.getMessage());
            }
        }
    }



    private RouteWithoutId createElement(){
        InputScanner = PromptScan.getUserScanner();

        System.out.println("Введите имя");
        String name = askString(InputScanner);

        System.out.println("Введите координату x:");
        float x = askX(InputScanner);

        System.out.println("Введите координату y:");
        int y = askY(InputScanner);

        Coordinates coordinates = new Coordinates(x, y);
        System.out.println("Введите координату x точки, из которой стартовали:");
        Double xFrom = askDouble(InputScanner);

        System.out.println("Введите координату y точки, из которой стартовали:");
        double yFrom = askDouble(InputScanner);
        System.out.println("Введите координату z точки, из которой стартовали:");
        int zFrom = askInt(InputScanner);

        LocationFrom from = new LocationFrom(xFrom, yFrom ,zFrom, name);
        System.out.println("Введите координату x точки, из которой стартовали:");
        Double xTo = askDouble(InputScanner);

        System.out.println("Введите координату y точки, из которой стартовали:");
        double yTo = askDouble(InputScanner);
        System.out.println("Введите координату z точки, из которой стартовали:");
        int zTo = askInt(InputScanner);

        LocationTo to = new LocationTo(xTo, yTo ,zTo, name);


        Date creationDate = new Date();


        System.out.println("Введите пробег");
        double distance = askDouble(InputScanner);

;

        return new RouteWithoutId(name, coordinates, creationDate, from, to, distance);
    }
}
