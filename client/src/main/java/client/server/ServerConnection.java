package client.server;

import client.service.TaskService;
import common.model.ServerResponse;
import common.model.TaskTypeFromClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private final BufferedReader input;
    private final BufferedReader keyboard;
    private final PrintWriter output;
    private final TaskService taskService;

    private boolean isLoggedIn;

    public ServerConnection(Socket server) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(server.getInputStream()));
        this.keyboard = new BufferedReader(new InputStreamReader(System.in));
        this.output = new PrintWriter(server.getOutputStream(), true);
        this.taskService = new TaskService(input, keyboard, output);
    }

    @Override
    public void run() {
        output.println(TaskTypeFromClient.CLIENT_ENTRANCE.getTaskPrefix());
        try {
            while (true) {
                String response = input.readLine();
                ServerResponse serverResponse = ServerResponse.valueOf(response);
                if (serverResponse == ServerResponse.HI) {
                    init();
                }
                sendNewTaskToServer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
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
        System.out.print("Enter '1' to send a file\nEnter '2' to delete a file \nEnter '3' to print all files on server\n>");
        String taskNumber = keyboard.readLine();

        switch (taskNumber) {
            case "1":
                taskService.sendFile();
                break;

            case "2":
                taskService.deleteFile();
                break;

            case "3":
                taskService.printFiles();
                break;

            default:
                break;
        }
    }
}
