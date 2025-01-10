import objects.Location;
import constans.HumanConstans;
import constans.PlaceConstans;
import objects.King;
import objects.April;
import objects.Trotti;
import objects.Entity;
import exceptions.EntityNotFoundException;
import exceptions.DuplicateEntityException;
import exceptions.UnauthorizedActionException;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Trotti trotti = new Trotti("Тротти", 140, HumanConstans.HAPPY, 15, PlaceConstans.PALACE, "123-456-7890");
        King king = new King("Король", 50, HumanConstans.DRUNK, 15, PlaceConstans.PALACE, "123-456-7890");
        April april = new April("Эйприл", 50, HumanConstans.SAD, 15, PlaceConstans.STABLE, "123-456-7890");
        Location location = new Location();
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(trotti);
        entities.add(april);
        entities.add(king);

        for (Entity entity : entities) {
            System.out.println(entity.getName() + " есть в массиве entities");
            try {
                entity.SaveToFile("people.txt");
                System.out.println("Информация сохранена: " + entity.getInfo());
            } catch (IOException e) {
                System.out.println("Ошибка при сохранении информации: " + e.getMessage());
            }
        }

        try {
            King duplicate = new King("Король", 50, HumanConstans.DRUNK, 15, PlaceConstans.PALACE, "123-456-7890");
            if (entities.contains(duplicate)) {
                throw new DuplicateEntityException("Сущность уже существует в коллекции: " + duplicate.getName());
            }
            entities.add(duplicate);
        } catch (DuplicateEntityException e) {
            System.out.println(e.getMessage());
        }

        location.incrementPeopleCount(trotti.getLocation());
        location.incrementPeopleCount(king.getLocation());
        location.incrementPeopleCount(april.getLocation());

        try {
            System.out.println("Где король: " + king.getLocation());
            System.out.println("Где Эйприл: " + april.getLocation());
            System.out.println("Где Тротти: " + trotti.getLocation());
        } catch (Exception e) {
            System.out.println("Ошибка в получении локаций: " + e.getMessage());
        }

        try {
            king.performAction(king, april);
            april.performAction(april, king);
            trotti.call(trotti, king);
        } catch (UnauthorizedActionException e) {
            System.out.println("Ошибка действия: " + e.getMessage());
        }

        location.incrementPeopleCount(trotti.getLocation());
        location.incrementPeopleCount(king.getLocation());
        location.incrementPeopleCount(april.getLocation());

        trotti.performAction(trotti, king);
        april.performAction(april, trotti);
        king.performAction(king, april);

        Robbers robbers = new Robbers(10, HumanConstans.ANGRY);
        robbers.printCount();
        robbers.condition(king, trotti, april, HumanConstans.ANGRY);
    }
}
