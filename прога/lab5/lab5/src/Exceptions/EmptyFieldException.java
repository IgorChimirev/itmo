package Exceptions;

/*
 * Этот класс представляет исключение, которое выбрасывается при обнаружении пустого поля, когда это недопустимо.
 */
public class EmptyFieldException extends Exception {

    /*
     * Создает новый объект EmptyFieldException с указанным сообщением.
     *
     * @param message сообщение, описывающее причину исключения
     */
    public EmptyFieldException(String message) {
        super(message);
    }
}
