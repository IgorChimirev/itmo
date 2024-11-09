package skills;

import ru.ifmo.se.pokemon.*;

public class RockTomb extends PhysicalMove {
    public RockTomb() {
        super(Type.ROCK, 60, 0.95);
    }

    @Override
    protected String describe() {
        return "использует RockTomb";
    }

    @Override
     protected void applyOppEffects(Pokemon p){
         p.setMod(Stat.SPEED, -1);
    }
}