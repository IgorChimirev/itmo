package skills;

import ru.ifmo.se.pokemon.*;

public class IceBeam extends SpecialMove {
    public IceBeam() {
        super(Type.ICE, 90, 1);
    }

    @Override
    protected String describe() {
        return "использует IceBeam";
    }

    @Override
     protected void applyOppEffects(Pokemon p) {
       if(Math.random() <= 0.1){
            Effect effect = new Effect();
            effect.freeze(p);
            p.addEffect(effect);
        }
        
    }
}