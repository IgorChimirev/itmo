package Modules;

import CollectionObject.Route;

import java.util.HashMap;
import java.util.Stack;

/*
 * Интерфейс {@code DataProvider} предоставляет методы для сохранения и загрузки коллекции объектов {@code Route}.
 * Этот интерфейс может быть реализован для обработки различных методов сохранения данных для коллекции.
 */
public interface DataProvider {
    void save(HashMap<Long, Route> collection);
    void load();
}