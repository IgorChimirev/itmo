package CollectionObject;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import Network.User;

public class RouteModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private final String name;
    private final Coordinates coordinates;
    private final Date creationDate = new Date();
    private final LocationFrom from;
    private final LocationTo to;
    private final double distance;
    private User user;

    public RouteModel(String name, Coordinates coordinates,
                      LocationFrom from, LocationTo to, double distance,User user) {
        this.name = validateName(name);
        this.coordinates = validateCoordinates(coordinates);
        this.from = validateLocationFrom(from);
        this.to = validateLocationTo(to);
        this.distance = validateDistance(distance);
        this.user = user;
    }

    // Валидаторы
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        return name.trim();
    }

    private Coordinates validateCoordinates(Coordinates coordinates) {
        return Objects.requireNonNull(coordinates, "Координаты обязательны");
    }

    private LocationFrom validateLocationFrom(LocationFrom from) {
        return Objects.requireNonNull(from, "Локация отправления обязательна");
    }

    private LocationTo validateLocationTo(LocationTo to) {
        return Objects.requireNonNull(to, "Локация назначения обязательна");
    }

    private double validateDistance(double distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("Дистанция должна быть положительной");
        }
        return distance;
    }

    // Геттеры
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public LocationFrom getFrom() {
        return from;
    }

    public LocationTo getTo() {
        return to;
    }

    public double getDistance() {
        return distance;
    }
    public User getUser() {
        return user;
    }

    // Сеттер для ID с валидацией
    public void setId(Long id) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteModel)) return false;
        RouteModel that = (RouteModel) o;
        return Double.compare(that.distance, distance) == 0 &&
                Objects.equals(id, that.id) &&
                name.equals(that.name) &&
                coordinates.equals(that.coordinates) &&
                from.equals(that.from) &&
                to.equals(that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, from, to, distance);
    }

    @Override
    public String toString() {
        return String.format(
                "RouteModel [ID=%d, Name='%s', Coordinates=%s, Created=%s, From=%s, To=%s, Distance=%.2f]",
                id, name, coordinates, creationDate, from, to, distance
        );
    }
}