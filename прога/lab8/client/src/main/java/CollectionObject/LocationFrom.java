package CollectionObject;


import java.io.Serializable;
import java.util.Objects;

public class LocationFrom implements  Serializable {
    private static final long serialVersionUID = 1L;

    private double x;
    private double y;
    private int z;
    private String name;

    public LocationFrom(double x, double y, int z, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название локации не может быть пустым");
        }
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name.trim();
    }

    public LocationFrom() {
        this.name = "default";
    }

    // Геттеры
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    // Сеттеры с валидацией
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название локации не может быть пустым");
        }
        this.name = name.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationFrom)) return false;
        LocationFrom that = (LocationFrom) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                z == that.z &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, name);
    }

    @Override
    public String toString() {
        return String.format(
                "LocationFrom [x=%.2f, y=%.2f, z=%d, name='%s']",
                x, y, z, name
        );
    }
}