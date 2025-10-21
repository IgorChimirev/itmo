import Modules.DBProvider;
import Modules.Server;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        // для сервака в тертий аргумент прописать pgpass
        //DBProvider.establishConnection("jdbc:postgresql://localhost:5432/studs", "s468013", "iqKjXPvPPWFfyhij");
        //Server server = new Server(new InetSocketAddress(2801));

//       Строчки для теста на локалхосте. Для гелиуса достаточно указать только порт
        DBProvider.establishConnection("jdbc:postgresql://localhost:5432/studs", "s468013", "123");
       Server server = new Server(new InetSocketAddress("localhost", 1231));
        server.run();
    }
}
