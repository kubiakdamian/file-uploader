package client.service;

import client.ClientData;
import client.ClientListener;
import client.FilesToSend;
import client.QueuedFiles;
import client.file.FileToProcess;
import common.model.Dictionary;
import common.model.serverResponse.FilesOnServerResponse;
import common.model.serverResponse.ServerResponse;
import common.model.serverResponse.SignInServerResponse;
import common.model.task.GetFilenamesTask;
import common.model.task.SignInUserTask;
import common.model.task.SignOutUserTask;
import common.model.task.SignUpUserTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskService {

    private final BufferedReader keyboard;
    private final ClientData clientData;
    private final ClientListener clientListener;

    public TaskService(BufferedReader keyboard, ClientData clientData) {
        this.keyboard = keyboard;
        this.clientData = clientData;
        this.clientListener = new ClientListener();
    }

    public boolean signIn() throws IOException {
        System.out.print("Enter your name: ");
        String name = keyboard.readLine();
        SignInUserTask task = new SignInUserTask(name);
        clientData.getServerUtils().sendTask(task);

        SignInServerResponse serverResponse = clientData.getServerUtils().fetchSignInServerResponse();

        if (serverResponse != null) {
            System.out.println(ServerResponse.SUCCESSFUL_SIGN_IN.getMessage());
            clientData.setDirectoryName(serverResponse.getDirectoryName());
            clientListener.start(clientData);
            synchronizeFilesOnSignIn();
            return true;
        } else {
            System.out.println("Something went wrong, please try again");
            return false;
        }
    }

    public void signOut() {
        SignOutUserTask signOutUserTask = new SignOutUserTask();
        clientData.getServerUtils().sendTask(signOutUserTask);

        clientData.getServerUtils().closeConnection();
        System.exit(0);
    }

    public void signUp() throws IOException {
        System.out.print("Enter your name and path directory divided by ':'. ex. john:johnFiles\n>");
        String taskData = keyboard.readLine();
        String[] userData = taskData.split(":");
        String userName = userData[0];
        String directoryName = userData[1];

        SignUpUserTask signUpUserTask = new SignUpUserTask(userName, directoryName);
        clientData.getServerUtils().sendTask(signUpUserTask);

        ServerResponse serverResponse = clientData.getServerUtils().fetchServerResponse();

        if (serverResponse == ServerResponse.SUCCESSFUL_SIGN_UP) {
            clientData.setName(userName);
            clientData.setDirectoryName(directoryName);
            createDirectory(directoryName);
            System.out.println(ServerResponse.SUCCESSFUL_SIGN_UP.getMessage());
        } else {
            System.out.println("Something went wrong, please try again");
        }
    }

    public void printFilesFromServer() {
        GetFilenamesTask getFilenamesTask = new GetFilenamesTask();
        clientData.getServerUtils().sendTask(getFilenamesTask);

        FilesOnServerResponse filesOnServerResponse = clientData.getServerUtils().fetchFilesFromServer();

        if (filesOnServerResponse != null) {
            for (String filename : filesOnServerResponse.getFiles()) {
                System.out.println(filename);
            }
        } else {
            System.out.println("Server directory is empty");
        }
    }

    public List<String> synchronizeFilesWithServer() {
        GetFilenamesTask getFilenamesTask = new GetFilenamesTask();
        clientData.getServerUtils().sendTask(getFilenamesTask);
        FilesOnServerResponse filesOnServerResponse = clientData.getServerUtils().fetchFilesFromServer();
        List<String> serverFileNames = new ArrayList<>();

        if (filesOnServerResponse != null) {
            System.out.println("Synchronizing files with server. Please wait...");
            List<String> localFiles = getLocalFiles();
            serverFileNames = filesOnServerResponse.getFiles();
            serverFileNames.removeAll(localFiles);

            printFilesToSynchronize(serverFileNames);
            addAllFilesToQueue(serverFileNames);
        }

        return serverFileNames;
    }

    private void createDirectory(String directoryName) throws IOException {
        String path = Dictionary.CLIENTS_DIRECTORY + "/" + directoryName;
        Files.createDirectory(Paths.get(path));
    }

    private List<String> getLocalFiles() {
        String directoryName = clientData.getDirectoryName();
        String path = Dictionary.CLIENTS_DIRECTORY + "/" + directoryName;

        File[] files = new File(path).listFiles();
        List<String> localFiles = new ArrayList<>();

        for (File file : Objects.requireNonNull(files)) {
            localFiles.add(file.getName());
        }

        return localFiles;
    }

    private void addAllFilesToQueue(List<String> files) {
        for (String file : files) {
            String[] fileData = file.split("#");
            String name = fileData[0];
            String size = fileData[1].replace(".txt", "");
            common.model.file.File aFile = new common.model.file.File(name, Integer.parseInt(size));
            FileToProcess fileToProcess = new FileToProcess(clientData, aFile);
            QueuedFiles.addFileToProcess(fileToProcess);
        }
    }

    private void printFilesToSynchronize(List<String> files) {
        System.out.println("Files to synchronize:");
        for (String file : files) {
            System.out.println(file);
        }
    }

    private void synchronizeFilesOnSignIn() {
        List<String> serverFiles = synchronizeFilesWithServer();
        List<String> localFiles = getLocalFiles();
        localFiles.removeAll(serverFiles);

        if (!localFiles.isEmpty()) {
            for (String file : localFiles) {
                String[] fileData = file.split("#");
                String name = fileData[0];
                String size = fileData[1].replace(".txt", "");
                common.model.file.File aFile = new common.model.file.File(name, Integer.parseInt(size));
                aFile.setType(common.model.file.File.Type.CREATE);
                FilesToSend.addFile(aFile);
            }
        }
    }
}
