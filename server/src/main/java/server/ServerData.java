package server;

import common.model.Client;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerData {

    private final Map<String, Client> signedUpClients = new HashMap<>();
    private final Map<String, Socket> signInClients = new HashMap<>();
    private int connectedClients = 0;

    public void signUpClient(String name, Client client) {
        signedUpClients.put(name, client);
        System.out.println("Signed up clients: " + signedUpClients.size());
    }

    public void signInClient(String name, Socket socket) {
        System.out.println("New client signed in");
        signInClients.put(name, socket);
        System.out.println("Currently signed in clients number: " + signInClients.size());
    }

    public boolean checkIfClientAlreadyExists(String name) {
        return signedUpClients.containsKey(name);
    }

    public boolean checkIfDirectoryAlreadyExists(String name) {
        for (Client client : signedUpClients.values()) {
            if (client.getDirectoryName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public boolean checkIfClientIsConnected(String name) {
        return signInClients.containsKey(name);
    }

    public int getSignedUpClientsSize() {
        return signInClients.size();
    }

    public int getConnectedClientsNumber() {
        return signInClients.size();
    }

    public void newClientConnected() {
        System.out.println("New client connected");
        connectedClients++;
        System.out.println("Currently connected clients: " + connectedClients);
    }
}
