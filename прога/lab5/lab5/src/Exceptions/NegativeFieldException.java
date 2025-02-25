package Exceptions;

/*
 * Этот класс представляет исключение, которое выбрасывается при обнаружении отрицательного значения в поле, где оно недопустимо.
 */
public class NegativeFieldException extends Exception {

    /*
     * Создает новый объект NegativeFieldException с указанным сообщением.
     *
     * @param message сообщение, описывающее причину исключения
     */
    public NegativeFieldException(String message) {
        super(message);
    }
}
