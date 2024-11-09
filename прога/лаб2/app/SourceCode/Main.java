import ru.ifmo.se.pokemon.*;
import abs.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        b.addAlly(new Virizion("Артем", 1));

        b.addAlly(new Nosepass("Антон", 2));

        b.addAlly(new Probopass("Игорь", 3));
    
        b.addFoe(new Lotad("Тимур", 3));
        
        b.addFoe(new Lombre("Ксения", 2));

        b.addFoe(new Ludicolo("Толик", 2));

        
        b.go();
    }
}