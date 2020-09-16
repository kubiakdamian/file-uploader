package server.service;

import common.model.Dictionary;
import common.model.serverResponse.FilesOnServerResponse;
import common.model.serverResponse.ServerResponse;
import common.model.serverResponse.SignInServerResponse;
import common.model.task.SignInUserTask;
import common.model.task.SignUpUserTask;
import common.model.task.Task;
import common.model.task.TaskWithFile;
import server.QueuedFiles;
import server.ServerData;
import server.client.ClientUtils;
import server.client.FileToProcess;
import server.model.Client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            Client clientByName = serverData.getClientByName(name);
            SignInServerResponse signInServerResponse = new SignInServerResponse(clientByName.getDirectoryName());
            clientUtils.sendSuccessfulSigningInResponse(signInServerResponse);
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
        TaskWithFile taskWithFile = (TaskWithFile) taskData;
        List<String> files = getFiles();

        if (!files.contains(taskWithFile.getFile().getFullName())) {
            FileToProcess fileToProcess = new FileToProcess(taskWithFile, clientUtils, serverData);
            QueuedFiles.addFileToProcess(fileToProcess);
        }
    }

    public void deleteFile(Task taskData) {
        FileToProcess fileToProcess = new FileToProcess((TaskWithFile) taskData, clientUtils, serverData);
        QueuedFiles.addFileToProcess(fileToProcess);
    }

    public void getFileNames() {
        List<String> files = getFiles();
        FilesOnServerResponse filesOnServerResponse = new FilesOnServerResponse(files);
        clientUtils.sendFilesOnServerResponse(filesOnServerResponse);
        clientUtils.sendResponse(ServerResponse.FILE_SENT_SUCCESSFULLY);
    }

    private List<String> getFiles() {
        Client client = serverData.getClientByName(clientUtils.getClientName());
        String directoryName = client.getDirectoryName();
        String path = Dictionary.SERVER_DIRECTORY + "/" + directoryName;

        File[] files = new File(path).listFiles();
        List<String> filesOnServer = new ArrayList<>();

        for (File file : Objects.requireNonNull(files)) {
            filesOnServer.add(file.getName());
        }

        return filesOnServer;
    }
}
