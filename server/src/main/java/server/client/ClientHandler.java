package server.client;

import common.model.Client;
import common.model.Dictionary;
import common.model.ServerResponse;
import common.model.TaskTypeFromClient;
import server.ServerData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientHandler implements Runnable {
    private final BufferedReader in;
    private final PrintWriter out;
    private final ServerData serverData;
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket, ServerData serverData) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientSocket = clientSocket;
        this.serverData = serverData;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String request = in.readLine();
                processRequest(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processRequest(String request) throws IOException {
        int requestDividerIndex = request.indexOf("|");
        String requestTaskType = request.substring(0, requestDividerIndex);
        String taskData = request.substring(requestDividerIndex + 1);

        TaskTypeFromClient taskTypeFromClient = TaskTypeFromClient.valueOf(requestTaskType);

        switch (taskTypeFromClient) {
            case CLIENT_ENTRANCE:
                out.println(ServerResponse.HI);
                break;

            case CREATE_USER:
                signUpUser(taskData);
                break;

            case SIGN_IN_USER:
                signInUser(taskData);
                break;

            case ADD_FILE:
                addFile(taskData);
                break;

            case DELETE_FILE:
                deleteFile(taskData);
                break;

            case GET_FILES:
                getFiles(taskData);
                break;

            default:
                break;
        }
    }

    private void signInUser(String name) {
        if (serverData.checkIfClientIsConnected(name)) {
            out.println(ServerResponse.FAILED_TO_SIGN_IN);
        } else {
            signIn(name);
        }
    }

    private void signIn(String name) {
        if (serverData.checkIfClientAlreadyExists(name)) {
            serverData.signInClient(name, clientSocket);
            out.println(ServerResponse.SUCCESSFUL_SIGN_IN);
        } else {
            out.println(ServerResponse.FAILED_TO_SIGN_IN);
        }
    }

    private void signUpUser(String taskData) throws IOException {
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

    private void addFile(String taskData) {
        System.out.println("Creating file...");
    }

    private void deleteFile(String taskData) {
        System.out.println("Removing file...");
    }

    private void getFiles(String taskData) {
        System.out.println("Get all files...");
    }
}
