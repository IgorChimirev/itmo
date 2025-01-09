package Objects;


import Constans.HumanConstans;
import Constans.PlaceConstans;
import interfaces.IHuman;

public class April extends Entity implements IHuman{
    
    public April(String name, int weight, HumanConstans condition,int Hp, PlaceConstans Location,String phone) {
        super(name, weight, condition,Hp,Location,phone);
    }
    @Override
    public void performAction(Entity first,Entity second) {
        HumanConstans currentCondition = first.getCondition();
        switch (currentCondition) {
            case ANGRY:
                System.out.println(first.getName() + " злая, так как не подошло платье,решила поехать за новым.");
                DrivingSomewhere(first, second);
                break;
            case HUNGRY:
                System.out.println(first.getName() + "голодна, поэтому пошла куда-нибудь перекусить.");
                Eat(first, second); 
                break;
            case SAD:
                System.out.println(first.getName() + "расстроена, поэтому решила покататься на лошадях.");
                HorseWalk(first,second);
                break;
            default:
                System.out.println("У" +first.getName() + "сегодня был тяжелый день.");
                first.setLocation(PlaceConstans.palace);
                System.out.println(first.getName() + " пошла спать, так как очень устала.");
                Dreaming(first);
            
        }
    
    }
    @Override
    public void DrivingSomewhere(Entity first, Entity second){
        second.setLocation(PlaceConstans.shop);
        first.setLocation(PlaceConstans.shop);
        second.setSize(second.getWeight()-10);
        first.setSize(first.getWeight()-10);
        System.out.println(getName() + " поехал вместе с " + second.getName() + " в " + first.getLocation());
        System.out.println(getConditionAdjective(second)+" "+second.getName() + " и " +getConditionAdjective(first) + first.getName() + "смогли найти новое платье "+ second.getWeight() + " " + first.getWeight()+".");
        this.setCondition(HumanConstans.NOTHUNGER);
        this.phone(first.getPhone());
    } 
    @Override
    public void Eat(Entity first,Entity second) {
        this.setLocation(PlaceConstans.cafe);
        second.setLocation(PlaceConstans.cafe);
        System.out.println(getName() + " поехал в "+this.getLocation() + " вместе с " + second.getName()+" так как они проголодались");
        System.out.println(first.getName() + " сейчас кушают с " + this.getName() + " в кафе.");
        second.setSize(second.getWeight()+10);
        first.setSize(first.getWeight()+10);
        second.setLocation(PlaceConstans.hospital);
        first.setLocation(PlaceConstans.hospital);
        System.out.println(first.getName() + "и" + second.getName() + "отравились  и теперь их  вес составляет "+ this.getWeight()+"и" +second.getWeight() + ". Теперь они отправились в "+second.getLocation()+".");
    }
    @Override
    public void HorseWalk(Entity first, Entity second) {
        int currentHp = first.getHp();
        currentHp -= 10;
        first.setHp(currentHp);
        first.setLocation(PlaceConstans.stable);
        System.out.println(this.getName() + " поехал в конюшню вместе с " + second.getName()+",чтобы покататься на лошадях.");
        System.out.println(first.getName() + " сейчас находится в " + first.getLocation() + " так как "+ second.getName() + " подвез его.");

    }
    public void Dreaming(Entity first) {
        int currentHp = this.getHp();
        currentHp -= 10;
        this.setHp(currentHp);
        this.setLocation(PlaceConstans.palace);
        System.out.println(this.getName() + " так как ей приснился жестокий сон.");
        System.out.println(this.getName() + " решила рассказать все,что произошло, "+first.getName());

    }
}
