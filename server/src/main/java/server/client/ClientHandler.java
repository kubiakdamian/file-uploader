package server.client;

import common.model.ServerResponse;
import common.model.TaskTypeFromClient;
import server.ServerData;
import server.service.TaskService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final BufferedReader input;
    private final PrintWriter output;
    private final TaskService taskService;

    public ClientHandler(Socket clientSocket, ServerData serverData) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.output = new PrintWriter(clientSocket.getOutputStream(), true);
        this.taskService = new TaskService(output, serverData, clientSocket);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String request = input.readLine();
                processRequest(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            output.close();
            try {
                input.close();
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
                output.println(ServerResponse.HI);
                break;

            case CREATE_USER:
                taskService.signUpUser(taskData);
                break;

            case SIGN_IN_USER:
                taskService.signInUser(taskData);
                break;

            case ADD_FILE:
                taskService.addFile(taskData);
                break;

            case DELETE_FILE:
                taskService.deleteFile(taskData);
                break;

            case GET_FILES:
                taskService.getFiles(taskData);
                break;

            default:
                break;
        }
    }
}
