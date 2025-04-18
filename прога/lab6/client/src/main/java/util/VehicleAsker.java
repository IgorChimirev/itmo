package util;

import CollectionObject.Coordinates;
import CollectionObject.LocationFrom;
import CollectionObject.LocationTo;
import CollectionObject.RouteModel;
import Exceptions.EmptyFieldException;
import Exceptions.NegativeFieldException;

import java.util.Date;
import java.util.Scanner;


public class VehicleAsker {

    public static RouteModel createElement() {
        Scanner InputScanner = PromptScan.getUserScanner();

        System.out.println("Введите имя:");
        String name = askString(InputScanner);

        System.out.println("Введите координату x:");
        float x = askX(InputScanner);

        System.out.println("Введите координату y:");
        int y = askY(InputScanner);

        Coordinates coordinates = new Coordinates(x, y);

        System.out.println("Введите координату x точки отправления:");
        Double xFrom = askDouble(InputScanner);

        System.out.println("Введите координату y точки отправления:");
        double yFrom = askDouble(InputScanner);

        System.out.println("Введите координату z точки отправления:");
        int zFrom = askInt(InputScanner);

        LocationFrom from = new LocationFrom(xFrom, yFrom, zFrom, name);

        System.out.println("Введите координату x точки назначения:");
        Double xTo = askDouble(InputScanner);

        System.out.println("Введите координату y точки назначения:");
        double yTo = askDouble(InputScanner);

        System.out.println("Введите координату z точки назначения:");
        int zTo = askInt(InputScanner);

        LocationTo to = new LocationTo(xTo, yTo, zTo, name);


        System.out.println("Введите расстояние:");
        double distance = askDouble(InputScanner);

        return new RouteModel(name, coordinates,from, to, distance);
    }


    private static String askString(Scanner inputScanner) {
        while (true) {
            try {
                String name = inputScanner.nextLine();
                if (name.isBlank()) {
                    throw new EmptyFieldException("Поле не может быть пустым. Введите его ещё раз: ");
                }
                return name.trim();
            } catch (EmptyFieldException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static float askX(Scanner InputScanner) {
        while (true) {
            try {
                return Float.parseFloat(InputScanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа. Введите его повторно:");
            }
        }
    }

    private static int askY(Scanner InputScanner) {
        while (true) {
            try {
                return Integer.parseInt(InputScanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа. Введите его повторно:");
            }
        }
    }

    private static float askFloat(Scanner InputScanner) {
        while (true) {
            try {
                float num = Float.parseFloat(InputScanner.nextLine());
                if (num > 0) {
                    return num;
                }
                throw new NegativeFieldException("Число не может быть отрицательным. Введите его ещё раз:");
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа. Введите его повторно:");
            } catch (NegativeFieldException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static double askDouble(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                System.out.println("[DEBUG] Введено: " + input);
                double num = Double.parseDouble(input);
                if (num > 0) {
                    return num;
                }
                throw new NegativeFieldException("Дистанция должна быть положительной");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка формата. Введите число (например, 123 или 123.5):");
            } catch (NegativeFieldException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static int askInt(Scanner InputScanner) {
        while (true) {
            try {
                int num = Integer.parseInt(InputScanner.nextLine());
                if (num > 0) {
                    return num;
                }
                throw new NegativeFieldException("Число не может быть отрицательным. Введите его ещё раз:");
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа. Введите его повторно:");
            } catch (NegativeFieldException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

