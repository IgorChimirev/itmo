package skills;
import java.util.Random;
import ru.ifmo.se.pokemon.*;

public class TriAttack extends SpecialMove {
    public TriAttack() {
        super(Type.NORMAL, 80, 1);
    }

    @Override
    protected String describe() {
        return "использует TriAttack";
    }

    @Override
     protected void applyOppEffects(Pokemon p) {
        if(Math.random() <= 0.2){
            Random random = new Random();
            int randomNumber = random.nextInt(3) + 1;
            switch (randomNumber){
                case 1:
                    Effect ef1 = new Effect();
                    ef1.burn(p);
                    p.addEffect(ef1);
                    break;
                case 2:
                    Effect ef2 = new Effect();
                    ef2.paralyze(p);
                    p.addEffect(ef2);
                    break;
                case 3:
                    Effect ef3 = new Effect();
                    ef3.freeze(p);
                    p.addEffect(ef3);
                    break;


            }
        }
    }
}