package skills;

import ru.ifmo.se.pokemon.*;

public class SwordsDance extends StatusMove {
    public SwordsDance() {
        super(Type.NORMAL, 0, 0);
    }

    @Override
    protected String describe() {
        return "использует SwordsDance";
    }
	@Override
    protected void applySelfEffects(Pokemon def) {
        
        def.setMod(Stat.ATTACK, 2);
        

}
}