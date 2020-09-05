package server.service;

import common.model.Client;
import common.model.Dictionary;
import common.model.ServerResponse;
import server.ServerData;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TaskService {

    private final PrintWriter out;
    private final ServerData serverData;
    private final Socket clientSocket;

    public TaskService(PrintWriter out, ServerData serverData, Socket clientSocket) {
        this.out = out;
        this.serverData = serverData;
        this.clientSocket = clientSocket;
    }

    public void signInUser(String name) {
        if (serverData.checkIfClientIsConnected(name)) {
            out.println(ServerResponse.FAILED_TO_SIGN_IN);
        } else {
            signIn(name);
        }
    }

    public void signIn(String name) {
        if (serverData.checkIfClientAlreadyExists(name)) {
            serverData.signInClient(name, clientSocket);
            out.println(ServerResponse.SUCCESSFUL_SIGN_IN);
        } else {
            out.println(ServerResponse.FAILED_TO_SIGN_IN);
        }
    }

    public void signUpUser(String taskData) throws IOException {
        String[] dividedTask = taskData.split(Dictionary.TASK_DIVIDER);
        String name = dividedTask[0];
        String directoryPath = dividedTask[1];

        if (!serverData.checkIfClientAlreadyExists(name) && !serverData.checkIfDirectoryAlreadyExists(directoryPath)) {
            Client client = new Client(name, directoryPath);
            String path = Dictionary.DATABASE_PATH + "/" + directoryPath;
            Files.createDirectory(Paths.get(path));

            serverData.signUpClient(name, client);
            System.out.println("Signed up user with name: " + name);
            out.println(ServerResponse.SUCCESSFUL_SIGN_UP);
        } else {
            out.println(ServerResponse.FAILED_TO_SIGN_UP);
        }
    }

    public void addFile(String taskData) {
        System.out.println("Creating file...");
    }

    public void deleteFile(String taskData) {
        System.out.println("Removing file...");
    }

    public void getFiles(String taskData) {
        System.out.println("Get all files...");
    }
}
