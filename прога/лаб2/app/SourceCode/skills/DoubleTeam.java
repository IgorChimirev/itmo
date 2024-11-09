package skills;

import ru.ifmo.se.pokemon.*;

public class DoubleTeam extends SpecialMove {
    public DoubleTeam() {
        super(Type.NORMAL, 0, 0);
    }

    @Override
    protected String describe() {
        return "использует DoubleTeam";
    }

    @Override
     protected void applyOppEffects(Pokemon p) {
        p.setMod(Stat.EVASION, 1);
        
    }
}