package Objects;


import Constans.HumanConstans;
import Constans.PlaceConstans;
import interfaces.IHuman;

public class King extends Entity implements IHuman{
    
    public King(String name, int weight, HumanConstans condition,int Hp, PlaceConstans Location,String phone) {
        super(name, weight, condition,Hp,Location,phone);
    }
    @Override
    public void performAction(Entity first,Entity second) {
        HumanConstans currentCondition = first.getCondition();
        switch (currentCondition) {
            case DRUNK:
                System.out.println(first.getName() + " хочет найти что-то, что можно выпить.");
                DrivingSomewhere(first, second);
                Fight(first, second);
                break;
            case HUNGRY:
                System.out.println(first.getName() + " голоден, поэтому он едет кушать вместе с "+second.getName()+".");
                Eat(first, second); 
                break;
            case UNHEALTHY:
                System.out.println("У" +first.getName() + " болит голова, поэтому поехал к кататься на лошадях вместе с  "+second.getName()+".");
                HorseWalk(first,second);
                break;
            default:
            System.out.println(first.getName() + " Как обычно решил выпить и нашел себе нового противника.");
            Fight(first,second);

        }
    }
    @Override
    public void DrivingSomewhere(Entity first, Entity second){
        second.setLocation(PlaceConstans.club);
        first.setLocation(PlaceConstans.club);
        second.setSize(second.getWeight()+50);
        first.setSize(first.getWeight()+50);
        System.out.println(getName() + " поехал вместе с " + second.getName() + " в " + first.getLocation()+"чтобы найти что-то выпить");
        System.out.println(getConditionAdjective(second)+" "+second.getName() + " и " +getConditionAdjective(first) + first.getName() + " наелись и напились и теперь они весят "+ second.getWeight() + " " + first.getWeight()+".");
        this.setCondition(HumanConstans.NOTHUNGER);
        this.phone(first.getPhone());
    } 
    @Override
    public void Eat(Entity first,Entity second) {
        first.setLocation(PlaceConstans.cafe);
        second.setLocation(PlaceConstans.cafe);
        System.out.println(this.getName() + " поехал в "+this.getLocation() + " вместе с " + second.getName()+" так как "+second.getName()+"позвал его на обед.");
        System.out.println(this.getName() + " сейчас кушают с " + second.getName() + " в" +this.getLocation());
        this.setSize(this.getWeight()+200);
        this.setHp(2);
        second.setLocation(PlaceConstans.hospital);
        System.out.println(this.getName() + "объелся и теперь его вес составляет "+ this.getWeight()+",а " + second.getName()+" отправился в "+second.getLocation()+" так как он отравился.");
    }
    @Override
    public void HorseWalk(Entity first, Entity second) {
        int currentHp = first.getHp();
        currentHp -= 10;
        first.setHp(currentHp);
        first.setLocation(PlaceConstans.stable);
        System.out.println(getName() + " поехал в кататься вместе с " + second.getName());
        System.out.println(first.getName() + " сейчас находится в " + first.getLocation() + " так как "+ second.getName() + " подвез его.");

    }
    public void Fight(Entity first, Entity second) {
        first.setLocation(PlaceConstans.polygon);
        second.setLocation(PlaceConstans.polygon);
        System.out.println(getName() + " поехал в "+this.getLocation() + " вместе с " + second.getName()+" так как они посорились в " + first.getLocation());
        System.out.println(first.getName() + " сейчас дерутся с " + second.getName() + " на полигоне по правилам рыцарских боев.");
        this.setSize(this.getWeight()-2);
        this.setHp(2);
        second.setLocation(PlaceConstans.hospital);
        System.out.println(this.getName() + "похудел и теперь его вес составляет "+ this.getWeight()+" а " + second.getName()+" отправился в "+second.getLocation()+" так как он проиграл.");
    }
}
