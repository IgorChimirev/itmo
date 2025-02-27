package Modules;

import CollectionObject.Route;

import java.util.Stack;

/*
 * Интерфейс {@code DataProvider} предоставляет методы для сохранения и загрузки коллекции объектов {@code Route}.
 * Этот интерфейс может быть реализован для обработки различных методов сохранения данных для коллекции.
 */
public interface DataProvider {

    /*
     * Сохраняет указанную коллекцию объектов {@code Route}.
     * Реализации должны определить, как коллекция сохраняется и где она хранится.
     *
     * @param collection коллекция {@code Stack} объектов {@code Route}, которая будет сохранена.
     */
    void save(Stack<Route> collection);

    /*
     * Загружает коллекцию объектов {@code Route}.
     * Реализации должны определить, как коллекция загружается из места её хранения.
     * Загруженные данные должны отражать текущее состояние сохраненных данных.
     */
    void load();
}
