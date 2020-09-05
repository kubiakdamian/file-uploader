package client.service;

import common.model.ServerResponse;
import common.model.TaskTypeFromClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class TaskService {

    private final BufferedReader input;
    private final BufferedReader keyboard;
    private final PrintWriter output;

    public TaskService(BufferedReader input, BufferedReader keyboard, PrintWriter output) {
        this.input = input;
        this.keyboard = keyboard;
        this.output = output;
    }

    public boolean signIn() throws IOException {
        System.out.print("Enter your name: ");
        String name = keyboard.readLine();
        String task = TaskTypeFromClient.SIGN_IN_USER.getTaskPrefix() + name;
        output.println(task);

        String response = input.readLine();
        ServerResponse serverResponse = ServerResponse.valueOf(response);

        if (serverResponse == ServerResponse.SUCCESSFUL_SIGN_IN) {
            System.out.println(ServerResponse.SUCCESSFUL_SIGN_IN.getMessage());
            return true;
        } else {
            System.out.println("Something went wrong, please try again");
            return false;
        }
    }

    public void signUp() throws IOException {
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

    public void sendFile() throws IOException {
        System.out.println("Enter filename and size divided by ':'. ex. file:8");
        String taskData = keyboard.readLine();

        output.println(TaskTypeFromClient.ADD_FILE.getTaskPrefix() + taskData);
    }

    public void deleteFile() throws IOException {
        System.out.println("Enter filename. ex. file");
        String taskData = keyboard.readLine();

        output.println(TaskTypeFromClient.ADD_FILE.getTaskPrefix() + taskData);
    }

    public void printFiles() {
        output.println(TaskTypeFromClient.GET_FILES.getTaskPrefix());
    }
}
