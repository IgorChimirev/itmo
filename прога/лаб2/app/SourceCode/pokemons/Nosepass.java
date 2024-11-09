package abs;



import ru.ifmo.se.pokemon.*;
import skills.*;

public class Nosepass extends Pokemon{
    public Nosepass(String name, int lvl){
        super(name, lvl);

        this.setStats(30, 45, 135, 45, 90, 30);
        this.setType(Type.NORMAL);
        this.setMove(new RockTomb(), new BullZone(),new EarthPower());
    }
}