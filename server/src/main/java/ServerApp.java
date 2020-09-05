import server.ServerInstance;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        ServerInstance serverInstance = new ServerInstance();
        serverInstance.start(args);
    }
}
