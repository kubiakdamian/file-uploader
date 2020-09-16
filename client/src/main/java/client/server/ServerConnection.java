package client.server;

import client.ClientData;
import client.service.TaskService;
import common.model.serverResponse.ServerResponse;
import common.model.task.ClientEntranceTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerConnection implements Runnable {
    private final BufferedReader keyboard;
    private final TaskService taskService;
    private final ClientData clientData;

    private boolean isLoggedIn;

    public ServerConnection(ClientData clientData) {
        this.keyboard = new BufferedReader(new InputStreamReader(System.in));
        this.taskService = new TaskService(keyboard, clientData);
        this.clientData = clientData;
    }

    @Override
    public void run() {
        clientData.getServerUtils().sendTask(new ClientEntranceTask());
        try {
            while (true) {
                ServerResponse serverResponse = clientData.getServerUtils().fetchServerResponse();
                if (serverResponse == ServerResponse.HI) {
                    init();
                }
                sendNewTaskToServer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientData.getServerUtils().getInput().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() throws IOException {
        while (!isLoggedIn) {
            System.out.print("Enter '1' to sing in\nEnter '2' to sign up\n>");
            String taskNumber = keyboard.readLine();

            switch (taskNumber) {
                case "1":
                    isLoggedIn = taskService.signIn();
                    break;

                case "2":
                    taskService.signUp();
                    break;

                default:
                    break;
            }
        }
    }

    private void sendNewTaskToServer() throws IOException {
        System.out.print("Enter '1' to sign out\nEnter '2' to print files from server\nEnter '3' to synchronize files with server\n>");
        String taskNumber = keyboard.readLine();

        switch (taskNumber) {
            case "1":
                taskService.signOut();
                break;

            case "2":
                taskService.printFilesFromServer();
                break;

            case "3":
                taskService.synchronizeFilesWithServer();
                break;

            default:
                break;
        }
    }
}
