package abs;

import ru.ifmo.se.pokemon.*;
import skills.*;

public class Lombre extends Lotad{
    public Lombre(String name, int lvl){
        super(name, lvl);

        this.setStats(60, 50, 50, 60, 70, 50);
        this.setType(Type.ROCK);
        this.setMove(new DoubleTeam(),new IceBeam(),new HydroPump());
    }
}