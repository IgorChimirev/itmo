package CollectionObject;

import java.util.Objects;

/*
 * Класс, представляющий местоположение с координатами x, y, z и именем.
 */
public class LocationFrom {
    private double x; // Поле не может быть null
    private double y;
    private int z;
    private String name;

    /*
     * Конструктор, создающий объект LocationFrom с заданными значениями x, y, z и именем.
     *
     * @param x    координата по оси x.
     * @param y    координата по оси y.
     * @param z    координата по оси z.
     * @param name имя места.
     */
    public LocationFrom(double x, double y, int z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    /*
     * Пустой конструктор по умолчанию.
     */
    public LocationFrom() {
    }

    /*
     * Возвращает значение координаты x.
     *
     * @return значение координаты x.
     */
    public double getX() {
        return x;
    }

    /*
     * Устанавливает значение координаты x.
     *
     * @param x новое значение координаты x.
     */
    public void setX(double x) {
        this.x = x;
    }

    /*
     * Возвращает значение координаты y.
     *
     * @return значение координаты y.
     */
    public double getY() {
        return y;
    }

    /*
     * Устанавливает значение координаты y.
     *
     * @param y новое значение координаты y.
     */
    public void setY(double y) {
        this.y = y;
    }

    /*
     * Возвращает значение координаты z.
     *
     * @return значение координаты z.
     */
    public double getZ() {
        return z;
    }

    /*
     * Устанавливает значение координаты z.
     *
     * @param z новое значение координаты z.
     */
    public void setZ(int z) {
        this.z = z;
    }

    /*
     * Возвращает имя местоположения.
     *
     * @return имя местоположения.
     */
    public String getName() {
        return name;
    }

    /*
     * Устанавливает имя местоположения.
     *
     * @param name новое имя местоположения.
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * Переопределяет метод equals для сравнения объектов по значениям координат x, y, z и имени.
     *
     * @param object объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        LocationFrom that = (LocationFrom) object;
        return Double.compare(y, that.y) == 0 &&
                Double.compare(x, that.x) == 0 &&
                z == that.z &&
                Objects.equals(name, that.name);
    }

    /*
     * Переопределяет метод hashCode для генерации хэш-кода на основе координат x, y, z и имени.
     *
     * @return хэш-код объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, name);
    }

    /*
     * Переопределяет метод toString для представления объекта в виде строки.
     *
     * @return строковое представление объекта.
     */
    @Override
    public String toString() {
        return "Координаты: (" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + name + '\'' +
                ')';
    }
}
