package client;

import client.file.FileProcessingScheduler;
import client.server.Server;
import client.server.ServerConnection;

import java.io.IOException;
import java.net.Socket;

public class ClientInstance {

    public void start(String[] args) throws IOException {
        if (args.length < 2) return;

        Server server = new Server(args);
        Socket socket = new Socket(server.getHost(), server.getPort());

        ClientData clientData = new ClientData(socket);
        ServerConnection serverConnection = new ServerConnection(clientData);

        FileProcessingScheduler fileProcessingScheduler = new FileProcessingScheduler();
        fileProcessingScheduler.start();

        new Thread(serverConnection).start();
    }
}
