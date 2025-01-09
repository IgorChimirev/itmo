package Objects;

import Constans.HumanConstans;
import Constans.PlaceConstans;
import interfaces.IHuman;


public class Trotti extends Entity implements IHuman {
    public Trotti(String name, int weight, HumanConstans condition, int hp, PlaceConstans location,String phone) {
        super(name, weight, condition, hp, location,phone);
    }
    @Override
    public void performAction(Entity first,Entity second) {
        HumanConstans currentCondition = first.getCondition();
        switch (currentCondition) {
            case HAPPY:
                System.out.println(first.getName() + " хочет прыгать от радости, поэтому пошла гулять в парк вместе с"+second.getName()+"в парк.");
                DrivingSomewhere(first, second);
                break;
            case HEALTHY:
                System.out.println(first.getName() + " здоров, поэтому пошла есть с "+second.getName());
                Eat(first, second); 
                break;
            case SAD:
                System.out.println(first.getName() + " грустит, поэтому поехала кататься на лошадях.");
                HorseWalk(first,second);
                break;
            default:
                System.out.println(first.getName() + " сидит дома и играет с куклами.");

        }
    }
    @Override
    public void DrivingSomewhere(Entity first,Entity second){
        if(Math.random() <0.5){
            second.setLocation(PlaceConstans.palace);
            first.setLocation(PlaceConstans.palace);
            first.setCondition(HumanConstans.SAD);
            second.setCondition(HumanConstans.SAD);
            System.out.println(this.getName()+"и"+ second.getName()+"попали под дождь, поэтому им пришлось поехать"+this.getLocation()+".");
            first.phone(first.getPhone());
            first.printHp(this,second);
        }else{
            first.setLocation(PlaceConstans.club);
            this.setLocation(PlaceConstans.club);
            this.setCondition(HumanConstans.HAPPY);
            first.setCondition(HumanConstans.HAPPY);
            this.phone(first.getPhone());
            System.out.println(second.getName()+" и "+ first.getName()+ "радостные поехали в " + first.getLocation()+".");
        }
    }
    @Override
    public void Eat(Entity first, Entity second) {
        int currentHp = first.getHp();
        currentHp += 10;
        if (currentHp < 0) {
            currentHp = 0;
        }
        first.setHp(currentHp);
        System.out.println(first.getName() + "получил +  " + 10 + " HP из-за того, что  " + second.getName()+"накормил его царскими деликатесами.");
        System.out.println("Теперь Hp "+ first.getName()+ " "+ first.getHp()+".");
    }
    @Override
    public void HorseWalk(Entity first, Entity second) {
        int currentHp = first.getHp();
        currentHp -= 10;
        first.setHp(currentHp);
        System.out.println(first.getName() + " получил -" + 10 + " HP из-за того что " + second.getName() + " врезался в него своей лощадью.");
        System.out.println("Теперь Hp "+ first.getName()+ " "+ first.getHp()+".");
    }
}
