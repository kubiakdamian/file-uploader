package server.service;

import common.model.Dictionary;
import common.model.ServerResponse;
import common.model.task.SignInUserTask;
import common.model.task.SignUpUserTask;
import common.model.task.Task;
import common.model.task.TaskWithFile;
import server.QueuedFiles;
import server.ServerData;
import server.client.ClientUtils;
import server.client.FileToProcess;
import server.model.Client;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TaskService {

    private final ClientUtils clientUtils;
    private final ServerData serverData;
    private final Socket clientSocket;

    public TaskService(ClientUtils clientUtils, ServerData serverData, Socket clientSocket) {
        this.clientUtils = clientUtils;
        this.serverData = serverData;
        this.clientSocket = clientSocket;
    }

    public void signInUser(Task request) {
        SignInUserTask task = (SignInUserTask) request;
        String name = task.getClientName();

        if (serverData.checkIfClientIsConnected(name)) {
            clientUtils.sendResponse(ServerResponse.FAILED_TO_SIGN_IN);
        } else {
            signIn(name);
        }
    }

    public void signIn(String name) {
        if (serverData.checkIfClientAlreadyExists(name)) {
            serverData.signInClient(name, clientSocket);
            clientUtils.setClientName(name);
            clientUtils.sendResponse(ServerResponse.SUCCESSFUL_SIGN_IN);
        } else {
            clientUtils.sendResponse(ServerResponse.FAILED_TO_SIGN_IN);
        }
    }

    public void signUpUser(Task request) throws IOException {
        SignUpUserTask task = (SignUpUserTask) request;
        String userName = task.getUserName();
        String directoryName = task.getDirectoryName();

        if (!serverData.checkIfClientAlreadyExists(userName) && !serverData.checkIfDirectoryAlreadyExists(directoryName)) {
            Client client = new Client(userName, directoryName);
            String path = Dictionary.SERVER_DIRECTORY + "/" + directoryName;
            Files.createDirectory(Paths.get(path));

            serverData.signUpClient(userName, client);
            System.out.println("Signed up user with name: " + userName);
            clientUtils.sendResponse(ServerResponse.SUCCESSFUL_SIGN_UP);
        } else {
            clientUtils.sendResponse(ServerResponse.FAILED_TO_SIGN_UP);
        }
    }

    public void signOutUser() {
        serverData.signOutClient(clientSocket);
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFile(Task taskData) {
        FileToProcess fileToProcess = new FileToProcess((TaskWithFile) taskData, clientUtils, serverData);
        QueuedFiles.addFileToProcess(fileToProcess);
    }

    public void deleteFile(Task taskData) {
        System.out.println("Removing file...");
    }

    public void getFiles(Task taskData) {
        System.out.println("Get all files...");
    }
}
