package client.server;

import common.model.ServerResponse;
import common.model.TaskTypeFromClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private final Socket server;
    private final BufferedReader input;
    private final BufferedReader keyboard;
    private final PrintWriter output;

    private boolean isLoggedIn;

    public ServerConnection(Socket server) throws IOException {
        this.server = server;
        this.input = new BufferedReader(new InputStreamReader(server.getInputStream()));
        this.keyboard = new BufferedReader(new InputStreamReader(System.in));
        this.output = new PrintWriter(server.getOutputStream(), true);
    }

    @Override
    public void run() {
        output.println(TaskTypeFromClient.CLIENT_ENTRANCE.getTaskPrefix());
        try {
            while (true) {
                String response = input.readLine();
                ServerResponse serverResponse = ServerResponse.valueOf(response);
                if (serverResponse == ServerResponse.HI) {
                    login();
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

    private void login() throws IOException {
        while (!isLoggedIn) {
            System.out.print("Enter '1' to sing in\nEnter '2' to sign up\n>");
            String taskNumber = keyboard.readLine();

            switch (taskNumber) {
                case "1":
                    signIn();
                    break;

                case "2":
                    signUp();
                    break;

                default:
                    break;
            }
        }
    }

    private void signIn() throws IOException {
        System.out.print("Enter your name: ");
        String name = keyboard.readLine();
        String task = TaskTypeFromClient.SIGN_IN_USER.getTaskPrefix() + name;
        output.println(task);

        String response = input.readLine();
        ServerResponse serverResponse = ServerResponse.valueOf(response);

        if (serverResponse == ServerResponse.SUCCESSFUL_SIGN_IN) {
            isLoggedIn = true;
            System.out.println(ServerResponse.SUCCESSFUL_SIGN_IN.getMessage());
        } else {
            System.out.println("Something went wrong, please try again");
        }
    }

    private void signUp() throws IOException {
        System.out.print("Enter your name and path directory divided by ':'. ex. john:johnFiles\n>");
        String taskData = keyboard.readLine();
        String task = TaskTypeFromClient.CREATE_USER.getTaskPrefix() + taskData;
        output.println(task);

        String response = input.readLine();
        ServerResponse serverResponse = ServerResponse.valueOf(response);

        if (serverResponse == ServerResponse.SUCCESSFUL_SIGN_UP) {
            System.out.println(ServerResponse.SUCCESSFUL_SIGN_UP.getMessage());
        } else {
            System.out.println("Something went wrong, please try again");
        }
    }

    private void sendNewTaskToServer() throws IOException {
        System.out.print("Enter '1' to send a file\nEnter '2' to delete a file \nEnter '3' to print all files on server\n>");
        String taskNumber = keyboard.readLine();

        switch (taskNumber) {
            case "1":
                sendFile();
                break;

            case "2":
                deleteFile();
                break;

            case "3":
                printFiles();
                break;

            default:
                break;
        }
    }

    private void sendFile() throws IOException {
        System.out.println("Enter filename and size divided by ':'. ex. file:8");
        String taskData = keyboard.readLine();

        output.println(TaskTypeFromClient.ADD_FILE.getTaskPrefix() + taskData);
    }

    private void deleteFile() throws IOException {
        System.out.println("Enter filename. ex. file");
        String taskData = keyboard.readLine();

        output.println(TaskTypeFromClient.ADD_FILE.getTaskPrefix() + taskData);
    }

    private void printFiles() {
        output.println(TaskTypeFromClient.GET_FILES.getTaskPrefix());
    }
}
