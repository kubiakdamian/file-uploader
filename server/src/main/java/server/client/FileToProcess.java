package server.client;

import common.model.Dictionary;
import common.model.serverResponse.ServerResponse;
import common.model.task.TaskTypeFromClient;
import common.model.task.TaskWithFile;
import lombok.Getter;
import server.QueuedFiles;
import server.ServerData;
import server.model.Client;
import server.simulator.FileProcessingSimulator;

import java.io.File;
import java.io.IOException;

@Getter
public class FileToProcess extends Thread {

    private final ClientUtils clientUtils;
    private final ServerData serverData;
    private final TaskWithFile task;
    private boolean isStopped = false;

    public FileToProcess(TaskWithFile task, ClientUtils clientUtils, ServerData serverData) {
        this.task = task;
        this.clientUtils = clientUtils;
        this.serverData = serverData;
    }

    @Override
    public void run() {
        if (task.getTaskType() == TaskTypeFromClient.ADD_FILE) {
            addFile();
        } else if (task.getTaskType() == TaskTypeFromClient.DELETE_FILE) {
            deleteFile();
        }
    }

    public synchronized void stopProcessing() {
        this.isStopped = true;
    }

    public synchronized void resumeProcessing() {
        this.isStopped = false;
        notify();
    }

    private void addFile() {
        FileProcessingSimulator.process(this);
        try {
            createFileOnServer();
            System.out.println("New file added: " + task.getFilename() + " client: " + clientUtils.getClientName());
            clientUtils.sendResponse(ServerResponse.FILE_SENT_SUCCESSFULLY);
            QueuedFiles.removeAlreadyProcessingFile(this);
        } catch (IOException e) {
            e.printStackTrace();
            clientUtils.sendResponse(ServerResponse.FAILED_TO_SEND_FILE);
        }
    }

    private void deleteFile() {
        FileProcessingSimulator.process(this);
        deleteFileFromServer();
        System.out.println("File deleted: " + task.getFilename() + " client: " + clientUtils.getClientName());
        clientUtils.sendResponse(ServerResponse.FILE_DELETED_SUCCESSFULLY);
        QueuedFiles.removeAlreadyProcessingFile(this);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createFileOnServer() throws IOException {
        Client client = serverData.getClientByName(clientUtils.getClientName());
        String clientDirectoryName = client.getDirectoryName();
        String filePathToCreate = Dictionary.SERVER_DIRECTORY + "/" + clientDirectoryName + "/" + task.getFilename() + ".txt";
        File file = new File(filePathToCreate);
        file.createNewFile();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteFileFromServer() {
        Client client = serverData.getClientByName(clientUtils.getClientName());
        String clientDirectoryName = client.getDirectoryName();
        String filePathToDelete = Dictionary.SERVER_DIRECTORY + "/" + clientDirectoryName + "/" + task.getFilename() + ".txt";
        File file = new File(filePathToDelete);
        file.delete();
    }
}
