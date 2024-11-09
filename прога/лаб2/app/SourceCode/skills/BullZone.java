package skills;

import ru.ifmo.se.pokemon.*;

public class BullZone extends PhysicalMove {
    public BullZone() {
        super(Type.GROUND, 60, 1);
    }

    @Override
    protected String describe() {
        return "использует BullZone";
    }

    @Override
     protected void applyOppEffects(Pokemon p) {
       p.setMod(Stat.SPEED, -1);
        
    }
}
