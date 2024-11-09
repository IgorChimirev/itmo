package abs;

import ru.ifmo.se.pokemon.*;
import skills.*;

public class Lotad extends Pokemon{
    public Lotad(String name, int lvl){
        super(name, lvl);

        this.setStats(40, 30, 30, 40, 50, 30);
        this.setType(Type.GHOST);
        this.setMove(new DoubleTeam(),new IceBeam());
    }
}