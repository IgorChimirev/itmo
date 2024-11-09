package abs;



import ru.ifmo.se.pokemon.*;
import skills.*;

public class Probopass extends Nosepass{
    public Probopass(String name, int lvl){
        super(name, lvl);

        this.setStats(60, 55, 145, 75, 150, 40);
        this.setType(Type.NORMAL);
        this.setMove(new RockTomb(), new BullZone(),new EarthPower(), new TriAttack());
    }
}