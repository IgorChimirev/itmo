package Modules;

import CollectionObject.*;
import Exceptions.*;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionService {
    protected static Long elementsCount = 0L;
    private Date initializationDate;
    public static HashMap<Long, Route> collection = new HashMap<>();
    private CompareRoutes comparator;
    protected static Scanner InputScanner;

    public CollectionService() {
        collection = new HashMap<>();
        this.initializationDate = new Date();
        comparator = new CompareRoutes();
    }

    private class CompareRoutes implements Comparator<Route> {
        @Override
        public int compare(Route o1, Route o2) {
            return o1.getName().compareTo(o2.getName());
        }

        @Override
        public Comparator<Route> reversed() {
            return Comparator.super.reversed();
        }
    }

    // Основные методы управления коллекцией
    public HashMap<Long, Route> add(Route element) {
        elementsCount += 1;
        Route newElement = new Route(
                elementsCount,
                element.getName(),
                element.getCoordinates(),
                new Date(),
                element.getFrom(),
                element.getTo(),
                element.getDistance()
        );
        collection.put(elementsCount, newElement);
        return collection;
    }

    public void insertElement(Long key, RouteModel element) throws NonExistingElementException {
        if (collection.containsKey(key)) {
            throw new NonExistingElementException("Элемент с ID " + key + " уже существует");
        }
        collection.put(key, new Route(
                key,
                element.getName(),
                element.getCoordinates(),
                new Date(),
                element.getFrom(),
                element.getTo(),
                element.getDistance()
        ));
        elementsCount = Math.max(elementsCount, key);
    }

    public HashMap<Long, Route> removeByKey(Long key) throws NonExistingElementException {
        if (collection.remove(key) == null) {
            throw new NonExistingElementException("Элемент с ID " + key + " не найден");
        }
        return collection;
    }

    public String info() {
        return "Тип коллекции: " + collection.getClass().getSimpleName() + "\n"
                + "Дата инициализации: " + initializationDate + "\n"
                + "Количество элементов: " + collection.size();
    }

    public List<Route> show() {
        return sortByName();
    }


    // Методы модификации
    public HashMap<Long, Route> update(long id, RouteModel element) throws NonExistingElementException {
        Route target = collection.get(id);
        if (target == null) {
            throw new NonExistingElementException("Элемента с id " + id + " не существует");
        }

        Route updatedRoute = new Route(
                id,
                element.getName(),
                element.getCoordinates(),
                target.getCreationDate(),
                element.getFrom(),
                element.getTo(),
                element.getDistance()
        );
        collection.put(id, updatedRoute);
        return collection;
    }

    public HashMap<Long, Route> removeById(long id) throws NonExistingElementException {
        if (collection.remove(id) == null) {
            throw new NonExistingElementException("Элемента с id " + id + " не существует");
        }
        return collection;
    }

    public HashMap<Long, Route> clear() {
        collection.clear();
        elementsCount = 0L;
        return collection;
    }

    // Фильтрация и сортировка
    public HashMap<Long, Route> removeLower(long startId) {
        collection.entrySet().removeIf(entry -> entry.getKey() < startId);
        return collection;
    }

    public List<Route> reorder() {
        List<Route> sorted = new ArrayList<>(collection.values());
        sorted.sort(comparator.reversed());
        return sorted;
    }

    public List<Route> filter_less_than_distance(double maxDistance) {
        return collection.values().stream()
                .filter(r -> r.getDistance() < maxDistance)
                .collect(Collectors.toList());
    }

    public List<Double> getDistancesDescending() {
        return collection.values().stream()
                .map(Route::getDistance)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    public List<Route> filterStartsWithName(String prefix) throws NonExistingElementException {
        List<Route> filtered = collection.values().stream()
                .filter(r -> r.getName().startsWith(prefix))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            throw new NonExistingElementException("Элементы с префиксом '" + prefix + "' не найдены");
        }
        return filtered.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    // Вспомогательные методы
    private List<Route> sortByName() {
        return collection.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public void replaceIfLower(Long key, double newDistance) {
        Route target = collection.get(key);
        if (target != null && newDistance < target.getDistance()) {
            Route updated = new Route(
                    key,
                    target.getName(),
                    target.getCoordinates(),
                    target.getCreationDate(),
                    target.getFrom(),
                    target.getTo(),
                    newDistance
            );
            collection.put(key, updated);
        }
    }

    public int countGreaterThanDistance(double distance) {
        return (int) collection.values().stream()
                .filter(r -> r.getDistance() > distance)
                .count();
    }
}