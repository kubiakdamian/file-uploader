import client.ClientInstance;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) throws IOException {
        ClientInstance clientInstance = new ClientInstance();
        clientInstance.start(args);
    }
}
