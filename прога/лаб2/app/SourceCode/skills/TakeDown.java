package skills;

import ru.ifmo.se.pokemon.*;

public class TakeDown extends PhysicalMove {
    public TakeDown() {
        super(Type.NORMAL, 90, 85);
    }

    @Override
    protected String describe() {
        return "использует TakeDown";
    }

    @Override
     protected void applySelfEffects(Pokemon def) {
        
        getStruggleMove();
    }
}