package CollectionObject;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import Network.User;

import java.util.Date;
import java.util.Objects;

/*
 * Класс, представляющий маршрут с уникальным идентификатором, именем, координатами, датой создания,
 * местами отправления и назначения, и расстоянием.
 */
public class Route implements Serializable {
    private Long id; // Поле не может быть null, значение должно быть больше 0, уникально и генерируется автоматически.
    private String name; // Поле не может быть null, строка не может быть пустой.
    private Coordinates coordinates; // Поле не может быть null.
    private Date creationDate; // Поле не может быть null, генерируется автоматически.
    private LocationFrom from; // Поле не может быть null, значение должно быть больше 0.
    private LocationTo to; // Поле не может быть null, значение должно быть больше 0.
    private double distance;
    private String creator;
    private static final long serialVersionUID = 1L;

    /*
     * Конструктор, создающий объект Route с заданными значениями.
     *
     * @param id           уникальный идентификатор маршрута.
     * @param name         имя маршрута.
     * @param coordinates  координаты маршрута.
     * @param creationDate дата создания маршрута.
     * @param from         место отправления.
     * @param to           место назначения.
     * @param distance     расстояние маршрута.
     */
    public Route(Long id, String name, Coordinates coordinates, Date creationDate, LocationFrom from, LocationTo to, double distance, String creator) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.creator = creator;
    }

    /*
     * Пустой конструктор по умолчанию.
     */
    public Route() {
    }

    /*
     * Возвращает идентификатор маршрута.
     *
     * @return идентификатор маршрута.
     */
    public Long getId() {
        return id;
    }

    /*
     * Устанавливает идентификатор маршрута.
     *
     * @param id новое значение идентификатора.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /*
     * Возвращает имя маршрута.
     *
     * @return имя маршрута.
     */
    public String getName() {
        return name;
    }

    /*
     * Устанавливает имя маршрута.
     *
     * @param name новое имя маршрута.
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * Возвращает координаты маршрута.
     *
     * @return координаты маршрута.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /*
     * Устанавливает координаты маршрута.
     *
     * @param coordinates новые координаты маршрута.
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /*
     * Возвращает дату создания маршрута.
     *
     * @return дата создания маршрута.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /*
     * Устанавливает дату создания маршрута.
     *
     * @param creationDate новая дата создания маршрута.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /*
     * Возвращает место отправления.
     *
     * @return место отправления.
     */
    public LocationFrom getFrom() {
        return from;
    }

    /*
     * Устанавливает место отправления.
     *
     * @param from новое место отправления.
     */
    public void setFrom(LocationFrom from) {
        this.from = from;
    }

    /*
     * Возвращает место назначения.
     *
     * @return место назначения.
     */
    public LocationTo getTo() {
        return to;
    }

    /*
     * Устанавливает место назначения.
     *
     * @param to новое место назначения.
     */
    public void setTo(LocationTo to) {
        this.to = to;
    }

    /*
     * Возвращает расстояние маршрута.
     *
     * @return расстояние маршрута.
     */
    public double getDistance() {
        return distance;
    }

    /*
     * Устанавливает расстояние маршрута.



     * @param distance новое значение расстояния.
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    /*
     * Переопределяет метод equals для сравнения объектов по значениям полей.
     *
     * @param object объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Route route = (Route) object;
        return Double.compare(distance, route.distance) == 0 &&
                Objects.equals(id, route.id) &&
                Objects.equals(name, route.name) &&
                Objects.equals(coordinates, route.coordinates) &&
                Objects.equals(creationDate, route.creationDate) &&
                Objects.equals(from, route.from) &&
                Objects.equals(to, route.to);
    }


    /*
     * Переопределяет метод hashCode для генерации хэш-кода на основе полей.
     *
     * @return хэш-код объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, from, to, distance);
    }

    /*
     * Переопределяет метод toString для представления объекта в виде строки.
     *
     * @return строковое представление объекта.
     */
    @Override
    public String toString() {
        return "Транспортное средство{" + "\n" +
                "id: " + id + "\n" +
                "Имя: " + name + "\n" +
                coordinates + "\n" +
                "Дата создания: " + creationDate + "\n" +
                "Откуда: " + from + "\n" +
                "Куда: " + to + "\n" +
                "Дистанция: " + distance + "\n" +
                ", \ncreator = '" + creator + '\'' +
                '}';
    }
}