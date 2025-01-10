import constans.HumanConstans;
import constans.PlaceConstans;
import objects.King;
import objects.April;
import objects.Trotti;
import objects.Entity;

public record Robbers(int count, HumanConstans condition) {
    public void printCount() {
        System.out.println("Количество разбойников: " + count);
    }

    public void condition(Entity first, Entity second, Entity third, HumanConstans condition) {
        switch (condition) {
            case ANGRY:
                first.setHp(0);
                second.setHp(0);
                System.out.println("Разбойники напали на дворец, где были " + first.getName() + ", " + second.getName()
                        + ", " + third.getName() + ", поэтому их здоровье: " + first.getHp() + ", " + second.getHp()
                        + ", " + third.getHp());
                break;
            case HAPPY:
                first.setWeight(first.getWeight() + 10);
                second.setWeight(second.getWeight() + 10);
                System.out.println("Довольные Разбойники ехали по лесу и всретили " + first.getName() + ", "
                        + second.getName() + ", " + third.getName()
                        + ". Они решили накормить их всякими вкусностями, поэтому их вес: " + first.getWeight() + " "
                        + second.getWeight());
                break;
            case HUNGER:
                first.setCondition(HumanConstans.SAD);
                second.setCondition(HumanConstans.SAD);
                System.out.println("Разбойники встретили " + first.getName() + ", " + second.getName() + ", "
                        + third.getName() + " и решили их ограбить, поэтому их настроение: " + first.getCondition()
                        + " " + second.getCondition());
                break;
        }
    }
}