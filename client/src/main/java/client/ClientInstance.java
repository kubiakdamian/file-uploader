package client;

import client.server.ServerConnection;
import common.model.ClientData;
import common.model.Server;

import java.io.IOException;
import java.net.Socket;

public class ClientInstance {

    public void start(String[] args) throws IOException {
        if (args.length < 2) return;

        Server server = new Server(args);
        Socket socket = new Socket(server.getHost(), server.getPort());

        ClientData clientData = new ClientData();
        ServerConnection serverConnection = new ServerConnection(socket, clientData);

        new Thread(serverConnection).start();
    }
}
