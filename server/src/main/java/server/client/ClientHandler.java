package server.client;

import common.model.ServerResponse;
import common.model.task.Task;
import common.model.task.TaskTypeFromClient;
import server.ServerData;
import server.service.TaskService;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final ClientUtils clientUtils;
    private final TaskService taskService;

    public ClientHandler(Socket clientSocket, ServerData serverData) throws IOException {
        this.clientUtils = new ClientUtils(clientSocket);
        this.taskService = new TaskService(clientUtils, serverData, clientSocket);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Task request = clientUtils.fetchClientTask();
                if (request != null) {
                    processRequest(request);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeClientConnections();
        }
    }

    private void closeClientConnections() {
        try {
            clientUtils.getOutput().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clientUtils.getInput().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processRequest(Task request) throws IOException {
        TaskTypeFromClient taskType = request.getTaskType();

        switch (taskType) {
            case CLIENT_ENTRANCE:
                clientUtils.sendResponse(ServerResponse.HI);
                break;

            case CREATE_USER:
                taskService.signUpUser(request);
                break;

            case SIGN_IN_USER:
                taskService.signInUser(request);
                break;

            case ADD_FILE:
                taskService.addFile(request);
                break;

            case DELETE_FILE:
                taskService.deleteFile(request);
                break;

            case GET_FILES:
                taskService.getFiles(request);
                break;

            case SIGN_OUT_USER:
                taskService.signOutUser();
                break;

            default:
                break;
        }
    }
}
