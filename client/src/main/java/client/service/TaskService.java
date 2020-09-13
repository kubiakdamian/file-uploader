package client.service;

import client.ClientData;
import client.ClientListener;
import common.model.Dictionary;
import common.model.ServerResponse;
import common.model.task.SignInUserTask;
import common.model.task.SignUpUserTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

        ServerResponse serverResponse = clientData.getServerUtils().fetchServerResponse();

        if (serverResponse == ServerResponse.SUCCESSFUL_SIGN_IN) {
            System.out.println(ServerResponse.SUCCESSFUL_SIGN_IN.getMessage());
            clientListener.start(clientData);
            return true;
        } else {
            System.out.println("Something went wrong, please try again");
            return false;
        }
    }

    public void signOut() {
        //TODO signing out
        System.out.println("Signing out...");
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

    private void createDirectory(String directoryName) throws IOException {
        String path = Dictionary.CLIENTS_DIRECTORY + "/" + directoryName;
        Files.createDirectory(Paths.get(path));
    }
}
