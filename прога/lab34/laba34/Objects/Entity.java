package Objects;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import Constans.HumanConstans;
import Constans.PlaceConstans;
public abstract class Entity {

    protected String name;
    protected int weight;
    protected HumanConstans condition;
    protected int hp;
    protected PlaceConstans location;
    protected String phone;

    public Entity(String name, int weight, HumanConstans condition, int hp, PlaceConstans location,String phone) {
        this.name = name;
        this.weight = weight;
        this.condition = condition;
        this.hp = hp;
        this.location = location;
        this.phone = phone;
    }
    public String getName() {
        return name;
    }
    public void saveToFile(String filename) throws IOException {
        throw new IOException("Ошибка записи в файл для " + name);
    }

    public void performAction(Entity e1, Entity e2, boolean isAuthorized) {
        if (!isAuthorized) {
            throw new UnauthorizedActionException("Действие несанкционировано для " + e1.getName());
        }
        // Логика действия
    }
    // Исключение для несанкционированных действий
class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
    public void SaveToFile(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(this.getInfo() + "\n");
        }
    }
    
    public String getInfo() {
        return "Имя: " + name + ", Вес: " + weight + ", Состояние: " + condition + ", HP: " + hp + ", Локация: " + location + ", Телефон: " + phone;
    }
    


    public int getWeight() {
        return weight;
    }


    public HumanConstans getCondition() {
        return condition;
    }

    public int getHp() {
        return hp;
    }
    public String getPhone() {
        return phone;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCondition(HumanConstans condition) {
        this.condition = condition;
    }


    public void setLocation(PlaceConstans location) {
        this.location = location;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(getName().toLowerCase(), entity.getName().toLowerCase());
    }

    public int hashCode() {
        return Objects.hash(getName().toLowerCase());
    }


    public String toString() {
        return getName();
    }

    public PlaceConstans getLocation() {
        return location; 
    }

    public void printHp(Entity object1,Entity object2) {
        System.err.println("Здоровье героев состовляет: "+object1.getHp()+" "+object2.getHp());
    }

    public void printLocation(Entity object) {
        System.err.println(object.getName() + " находится в " + object.getLocation());
    }

    public void setSize(int weight) {
        this.weight = weight;
    }
    public String getConditionAdjective(Entity first) {
        switch (first.getCondition()) {
            case HUNGER:
                return "голодный ";
            case NOTHUNGER:
                return "неголодный ";
            case HAPPY:
                return "радостный ";
            case DRUNK:
                return "пьяный ";
            default:
                return "неизвестное состояние";
        }
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("[\\s-]", "");
        if (phoneNumber.length() == 10 || phoneNumber.length() == 11) {
            for (char c : phoneNumber.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true; 
        }
        return false; 
    }
    public void phone(String PhoneNumber){
        if (isValidPhoneNumber(PhoneNumber)){
            System.out.println("--------СОЕДИНЕНИЕ УСТАНОВЛЕНО--------");
                switch (this.getCondition()) {
                    case NOTHUNGER:
                        this.setSize(getWeight()+10);
                        System.out.println(this.getName()+" смог дозвониться и попросить прислугу принести ему еды, теперь его вес "+getWeight()); 
                    case WOUNDED:
                        this.setLocation(PlaceConstans.hospital);
                        System.out.println(this.getName()+" смог дозвониться и вызвал себе скорую, теперь он находится в "+this.getLocation());
                    
                }
        }else{
            System.out.println("--------СОЕДИНЕНИЕ НЕ УСТАНОВЛЕНО--------");
            System.out.println(this.getName()+" не смог дозвониться"); 
        }
    }

    public void call(Entity first, Entity second){
        if (isValidPhoneNumber(first.getPhone()) && isValidPhoneNumber(second.getPhone())){
            System.out.println(first.getName()+" и " + second.getName()+" пошли ловить покемонов.");
            first.setCondition(HumanConstans.HAPPY); 
            second.setCondition(HumanConstans.HAPPY); 
        }else if(isValidPhoneNumber(first.getPhone())  && !isValidPhoneNumber(second.getPhone())){
            System.out.println(first.getName()+" и " + second.getName()+" пошли ловить покупать новый телефон.");
        }else if(!isValidPhoneNumber(first.getPhone())  && !isValidPhoneNumber(second.getPhone())){
            System.out.println(first.getName()+" и " + second.getName()+" выкинули свои телефоны.");
            first.setPhone("0");
            second.setPhone("0");
        }
    } 
 }