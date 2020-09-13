package client.service;

import client.ClientData;
import client.FilesToSend;
import common.model.ServerResponse;
import common.model.file.File;
import common.model.task.DeleteFileTask;
import common.model.task.SendFileTask;

public class FileSender extends Thread {

    private final ClientData clientData;

    public FileSender(ClientData clientData) {
        this.clientData = clientData;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (FilesToSend.getFilesSize() > 0) {
                File file = FilesToSend.getFirstFileToSend();
                sendFile(file);
            }
        }
    }

    private void sendFile(File file) {
        if (file.isCreateType()) {
            SendFileTask sendFileTask = new SendFileTask(file);
            clientData.getServerUtils().sendTask(sendFileTask);
            System.out.println("Sent file to server: " + file.getName());
            ServerResponse serverResponse = clientData.getServerUtils().fetchServerResponse();
            System.out.println(serverResponse.getMessage());

        } else if (file.isDeleteType()) {
            DeleteFileTask deleteFileTask = new DeleteFileTask(file);
            clientData.getServerUtils().sendTask(deleteFileTask);
        }
    }
}
