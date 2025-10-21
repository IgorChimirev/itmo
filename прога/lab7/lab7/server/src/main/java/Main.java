import Modules.DBProvider;
import Modules.Server;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        // для сервака в тертий аргумент прописать pgpass
        //DBProvider.establishConnection("jdbc:postgresql://pg:5432/studs", "s468013", "rwSy/3654");
        //Server server = new Server(new InetSocketAddress(9001));

//       Строчки для теста на локалхосте. Для гелиуса достаточно указать только порт
        DBProvider.establishConnection("jdbc:postgresql://localhost:5432/studs", "s468013", "123");
        Server server = new Server(new InetSocketAddress("localhost", 9000));
        server.run();
    }
}
