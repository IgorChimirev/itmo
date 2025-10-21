package Modules;

import CollectionObject.*;
import Exceptions.*;
import Network.User;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CollectionService {
    protected static Long elementsCount = 0L;
    private Date initializationDate;
    public static HashMap<Long, Route> collection = new HashMap<>();
    private CompareRoutes comparator;
    private ReentrantLock locker;

    public CollectionService() {
        collection = new HashMap<>();
        this.initializationDate = new Date();
        comparator = new CompareRoutes();
        this.locker = new ReentrantLock();
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
    public HashMap<Long, Route> add(RouteModel element, User user) throws DBProviderException {
        locker.lock();
        try {
            if (DBProvider.addRoute(element, user)) {
                elementsCount += 1;
                Route newElement = new Route(
                        elementsCount,
                        element.getName(),
                        element.getCoordinates(),
                        new Date(), // Дата создается заново
                        element.getFrom(), // LocationFrom
                        element.getTo(),    // LocationTo
                        element.getDistance(),
                        user.getUsername()
                );
                collection.put(elementsCount, newElement);
                return collection;
            }
            throw new DBProviderException("Ошибка при добавлении элемента");
        } finally {
            locker.unlock();
        }
    }
    public HashMap<Long, Route> clearAll() throws DBProviderException {
        locker.lock();
        try {
            if (DBProvider.clearAllRoutes()) { // Предполагается, что метод clearAllRoutes() существует в DBProvider
                collection.clear();
                elementsCount = 0L;
                return collection;
            }
            throw new DBProviderException("Ошибка при очистке коллекции");
        } finally {
            locker.unlock();
        }
    }
    public List<Route> filterByUser(String username) {
        return collection.values().stream()
                .filter(route -> route.getCreator().equals(username))
                .collect(Collectors.toList());
    }
    public List<Route> filterByUsername(String username) {
        return collection.values().stream()
                .filter(route -> route.getCreator().equals(username))
                .collect(Collectors.toList());
    }
    public void addRandomElements(User user) throws DBProviderException {
        locker.lock();
        try {
            Random random = new Random();
            for (int i = 0; i < 1000; i++) {
                // Генерация координат (x: int, y: float)
                Coordinates coordinates = new Coordinates(
                        random.nextInt(100), // x (int)
                        random.nextFloat() * 100 // y (float)
                );

                // Генерация LocationFrom (x, y: double; z: int)
                LocationFrom from = new LocationFrom(
                        random.nextDouble() * 1000, // x
                        random.nextDouble() * 1000, // y
                        random.nextInt(100), // z
                        "default" // название
                );

                // Генерация LocationTo (аналогично)
                LocationTo to = new LocationTo(
                        random.nextDouble() * 1000, // x
                        random.nextDouble() * 1000, // y
                        random.nextInt(100), // z
                        "default" // название
                );

                // Генерация distance (double > 0)
                double distance;
                do {
                    distance = random.nextDouble() * 1000;
                } while (distance <= 0);

                // Создание RouteModel
                RouteModel model = new RouteModel(
                        "RandomName" + random.nextInt(1000), // name
                        coordinates, // Coordinates
                        from, // LocationFrom
                        to, // LocationTo
                        distance, // distance
                        user // User
                );

                // Добавление в коллекцию
                this.add(model, user);
            }
        } finally {
            locker.unlock();
        }
    }


    public void insertElement(Long key, RouteModel element, User user) throws NonExistingElementException, DBProviderException {
        locker.lock();
        try {
            if (collection.containsKey(key)) {
                throw new NonExistingElementException("Элемент с ID " + key + " уже существует");
            }
            if (DBProvider.insertRoute(key, element, user)) {
                collection.put(key, new Route(
                        key,
                        element.getName(),
                        element.getCoordinates(),
                        new Date(),
                        element.getFrom(),
                        element.getTo(),
                        element.getDistance(),
                        user.getUsername()
                ));
                elementsCount = Math.max(elementsCount, key);
            }
        } finally {
            locker.unlock();
        }
    }

    public HashMap<Long, Route> removeByKey(Long key, User user) throws NonExistingElementException, DBProviderException {
        locker.lock();
        try {
            if (!collection.containsKey(key)) {
                throw new NonExistingElementException("Элемент с ID " + key + " не найден");
            }
            if (DBProvider.removeRoute(key, user)) {
                collection.remove(key);
                return collection;
            }
            throw new DBProviderException("Ошибка при удалении элемента");
        } finally {
            locker.unlock();
        }
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
    public HashMap<Long, Route> update(long id, RouteModel element, User user) throws NonExistingElementException, DBProviderException {
        locker.lock();
        try {
            Route target = collection.get(id);
            if (target == null || !target.getCreator().equals(user.getUsername())) {
                throw new NonExistingElementException("Элемента с id " + id + " не существует");
            }

            if (DBProvider.updateRoute(id, element, user)) {
                Route updatedRoute = new Route(
                        id,
                        element.getName(),
                        element.getCoordinates(),
                        target.getCreationDate(),
                        element.getFrom(),
                        element.getTo(),
                        element.getDistance(),
                        user.getUsername()
                );
                collection.put(id, updatedRoute);
                return collection;
            }
            throw new DBProviderException("Ошибка при обновлении элемента");
        } finally {
            locker.unlock();
        }
    }

    public HashMap<Long, Route> removeById(long id, User user) throws NonExistingElementException, DBProviderException {
        locker.lock();
        try {
            if (!collection.containsKey(id)) {
                throw new NonExistingElementException("Элемента с id " + id + " не существует");
            }
            if (DBProvider.removeRoute(id, user)) {
                collection.remove(id);
                return collection;
            }
            throw new DBProviderException("Ошибка при удалении элемента");
        } finally {
            locker.unlock();
        }
    }

    public HashMap<Long, Route> clear(User user) throws DBProviderException {
        locker.lock();
        try {
            if (DBProvider.clearRoutes(user)) {
                collection.entrySet().removeIf(entry -> entry.getValue().getCreator().equals(user.getUsername()));
                elementsCount = 0L;
                return collection;
            }
            throw new DBProviderException("Ошибка при очистке коллекции");
        } finally {
            locker.unlock();
        }
    }

    // Фильтрация и сортировка
    public HashMap<Long, Route> removeLower(long startId, User user) throws DBProviderException {
        locker.lock();
        try {
            if (DBProvider.removeRoutesLowerThanId(startId, user)) {
                collection.entrySet().removeIf(entry -> entry.getKey() < startId
                        && entry.getValue().getCreator().equals(user.getUsername()));
                return collection;
            }
            throw new DBProviderException("Ошибка при удалении элементов");
        } finally {
            locker.unlock();
        }
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

    public void replaceIfLower(Long key, double newDistance, User user) throws DBProviderException {
        locker.lock();
        try {
            Route target = collection.get(key);
            if (target != null && target.getCreator().equals(user.getUsername())
                    && newDistance < target.getDistance()) {
                if (DBProvider.updateRouteDistance(key, newDistance, user)) {
                    Route updated = new Route(
                            key,
                            target.getName(),
                            target.getCoordinates(),
                            target.getCreationDate(),
                            target.getFrom(),
                            target.getTo(),
                            newDistance,
                            user.getUsername()
                    );
                    collection.put(key, updated);
                }
            }
        } finally {
            locker.unlock();
        }
    }

    public int countGreaterThanDistance(double distance) {
        return (int) collection.values().stream()
                .filter(r -> r.getDistance() > distance)
                .count();
    }
}