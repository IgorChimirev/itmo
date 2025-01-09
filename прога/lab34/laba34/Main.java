import Objects.Location;
import Constans.HumanConstans;
import Constans.PlaceConstans;
import Objects.King;
import Objects.April;
import Objects.Trotti;
import Objects.Entity;
import java.io.IOException;
import java.util.ArrayList;

class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
public class Main {
    public record Robbers(int count, HumanConstans condition) {
        public void printCount() {
            System.out.println("Количество разбойников: " + count);
        }

        public void Condition(Entity first, Entity second,Entity third, HumanConstans condition) {
            switch (condition) {
                case ANGRY:
                    first.setHp(0);
                    second.setHp(0);
                    System.out.println("Разбойники напали на дворец, где были " + first.getName() + ", " + second.getName() + ", " + third.getName() + ",поэтому их здоровье: " + first.getHp() + ", " + second.getHp()+", " + third.getHp());
                    break;
                case HAPPY:
                    first.setWeight(first.getWeight() + 10);
                    second.setWeight(second.getWeight() + 10);
                    System.out.println("Довольные Разбойники ехали по лесу и всретили  " + first.getName() + " , " + second.getName() + ", " + third.getName() +". Они решили накормить их всякими вкусностями, поэтому их вес: " + first.getWeight() + " " + second.getWeight());
                    break;
                case HUNGER:
                    first.setCondition(HumanConstans.SAD);
                    second.setCondition(HumanConstans.SAD);
                    System.out.println("Разбойники встретили  " + first.getName() + "," + second.getName() + ", " + third.getName() +" и решили их ограбить, поэтому их настроение: " + first.getCondition() + " " + second.getCondition());
                    break;
            }
        }
    }
    
    
    public static void main(String[] args) {
        Trotti trotti = new Trotti("Тротти", 140, HumanConstans.HAPPY, 15, PlaceConstans.palace, "123-456-7890");
        King king = new King("Король", 50, HumanConstans.DRUNK, 15, PlaceConstans.palace, "123-456-7890");
        April april = new April("Эйприл", 50, HumanConstans.SAD, 15, PlaceConstans.stable, "123-456-7890");
        Location location = new Location();
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(trotti);
        entities.add(april);
        entities.add(king);
        

        for (Entity entity : entities) {
            System.out.println(entity.getName() + " есть в массиве Entities");
            try {
                entity.SaveToFile("people.txt"); // Попытка сохранить информацию в файл
                System.out.println("Информация сохранена: " + entity.getInfo());
            } catch (IOException e) { // Обработка исключения
                System.out.println("Ошибка при сохранении информации: " + e.getMessage());
            }
        }

        // Пример использования unchecked исключения DuplicateEntityException
        try {
            King duplicate = new King("Король", 50, HumanConstans.DRUNK, 15, PlaceConstans.palace, "123-456-7890");
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
        // Пример работы с локациями
        try {
            System.out.println("Где король: " + king.getLocation());
            System.out.println("Где эйприл: " + april.getLocation());
            System.out.println("Где Тротти: " + trotti.getLocation());
        } catch (Exception e) {
            System.out.println("Ошибка в получении локаций: " + e.getMessage());
        }

        // Пример использования unauthorized action
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
        trotti.performAction(trotti,king);
        april.performAction(april,trotti);
        king.performAction(king,april);


        Robbers robbers = new Robbers(10, HumanConstans.ANGRY);
        robbers.printCount();
        robbers.Condition(king, trotti,april, HumanConstans.ANGRY);

    }
}
