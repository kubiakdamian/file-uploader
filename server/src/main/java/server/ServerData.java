package server;

import server.model.Client;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerData {

    private final Map<String, Client> signedUpClients = new HashMap<>();
    private final Map<String, Socket> signedInClients = new HashMap<>();

    public void signUpClient(String name, Client client) {
        signedUpClients.put(name, client);
        System.out.println("Signed up clients: " + signedUpClients.size());
    }

    public void signInClient(String name, Socket socket) {
        System.out.println("New client signed in");
        signedInClients.put(name, socket);
        System.out.println("Currently signed in clients number: " + signedInClients.size());
    }

    public void signOutClient(Socket socket) {
        String nameBySocket = findNameBySocket(socket);
        signedInClients.remove(nameBySocket);
        System.out.println("Signed out client: " + nameBySocket);
        System.out.println("Currently signed in clients number: " + signedInClients.size());
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
        return signedInClients.containsKey(name);
    }

    public void newClientConnected() {
        System.out.println("New client connected");
    }

    public Client getClientByName(String name) {
        return signedUpClients.get(name);
    }

    private String findNameBySocket(Socket socket) {
        return signedInClients.entrySet()
                .stream()
                .filter(entry -> socket.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.joining());
    }
}
