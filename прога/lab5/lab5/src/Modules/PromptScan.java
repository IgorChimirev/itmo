package Modules;

import java.util.Scanner;

/*
 * Класс PromptScan предоставляет механизм управления единственным
 * экземпляром объекта Scanner, позволяя получать ввод от пользователя
 * в приложении.
 */
public class PromptScan {

    /*
     * Приватный статический экземпляр Scanner для обработки ввода от пользователя.
     */
    private static Scanner userScanner;

    /*
     * Возвращает текущий экземпляр Scanner для ввода от пользователя.
     *
     * @return текущий экземпляр userScanner
     */
    public static Scanner getUserScanner() {
        return userScanner;
    }

    /*
     * Устанавливает экземпляр Scanner, который будет использоваться для
     * ввода от пользователя. Это позволяет гибко управлять источниками ввода,
     * например, для тестирования или динамического изменения потока ввода.
     *
     * @param userScanner экземпляр Scanner для установки
     */
    public static void setUserScanner(Scanner userScanner) {
        PromptScan.userScanner = userScanner;
    }
}
