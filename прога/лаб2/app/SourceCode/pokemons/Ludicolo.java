package abs;



import ru.ifmo.se.pokemon.*;
import skills.*;

public class Ludicolo extends Lombre{
    public Ludicolo(String name, int lvl){
        super(name, lvl);

        this.setStats(80, 70, 70, 90, 100, 70);
        this.setType(Type.NORMAL);
        this.setMove(new DoubleTeam(),new IceBeam(),new HydroPump(), new SwordsDance());
    }
}