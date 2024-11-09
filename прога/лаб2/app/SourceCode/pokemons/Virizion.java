
package abs;



import ru.ifmo.se.pokemon.*;
import skills.*;

public class Virizion extends Pokemon{
    public Virizion(String name, int lvl){
        super(name, lvl);

        this.setStats(91, 90, 72, 90, 129, 108);
        this.setType(Type.ROCK);
        this.setMove(new FocusBlast(), new DoubleTeam(),new TakeDown(), new X_Scissor());
    }
}