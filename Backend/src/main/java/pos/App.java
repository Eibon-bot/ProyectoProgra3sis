package pos;

import pos.logic.Server;

public class App {

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
