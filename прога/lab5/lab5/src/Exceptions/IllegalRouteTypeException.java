package Exceptions;

/*
 * Этот класс представляет исключение, которое выбрасывается при использовании недопустимого типа маршрута.
 */
public class IllegalRouteTypeException extends Exception {

    /*
     * Создает новый объект IllegalRouteTypeException с указанным сообщением.
     *
     * @param message сообщение, описывающее причину исключения
     */
    public IllegalRouteTypeException(String message) {
        super(message);
    }
}
