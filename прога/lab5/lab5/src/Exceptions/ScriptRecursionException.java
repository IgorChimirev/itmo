package Exceptions;

/*
 * Этот класс представляет исключение, которое выбрасывается при обнаружении рекурсии в сценарии выполнения.
 */
public class ScriptRecursionException extends Exception {

    /*
     * Создает новый объект ScriptRecursionException с указанным сообщением.
     *
     * @param message сообщение, описывающее причину исключения
     */
    public ScriptRecursionException(String message) {
        super(message);
    }
}
