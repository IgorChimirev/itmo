package CollectionObject;

import java.util.Objects;

/*
 * Класс, представляющий координаты с двумя полями: x и y.
 * Поле x не может быть null.
 */
public class Coordinates {
    private Float x; // Поле не может быть null
    private int y;

    /*
     * Конструктор, создающий объект Coordinates с заданными значениями x и y.
     *
     * @param x координата по оси x. Не может быть null.
     * @param y координата по оси y.
     */
    public Coordinates(Float x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
     * Пустой конструктор по умолчанию.
     */
    public Coordinates() {
    }

    /*
     * Возвращает значение координаты x.
     *
     * @return значение координаты x.
     */
    public Float getX() {
        return x;
    }

    /*
     * Устанавливает значение координаты x.
     *
     * @param x новое значение координаты x. Не может быть null.
     */
    public void setX(Float x) {
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
    public void setY(int y) {
        this.y = y;
    }

    /*
     * Переопределяет метод equals для сравнения объектов по значениям координат x и y.
     *
     * @param object объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Coordinates that = (Coordinates) object;
        return Double.compare(y, that.y) == 0 && Objects.equals(x, that.x);
    }

    /*
     * Переопределяет метод hashCode для генерации хэш-кода на основе координат x и y.
     *
     * @return хэш-код объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
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
                ')';
    }
}
