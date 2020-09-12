package client.service;

import client.ClientData;
import common.model.file.File;
import common.model.task.DeleteFileTask;
import common.model.task.SendFileTask;

import java.util.LinkedList;
import java.util.Queue;

public class FileSender extends Thread {

    private final ClientData clientData;
    private Queue<File> files = new LinkedList<>();

    public FileSender(ClientData clientData) {
        this.clientData = clientData;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            processFile();
        }
    }

    public void addFile(File file) {
        files.add(file);
    }

    private void processFile() {
        if (!files.isEmpty()) {
            File file = files.poll();
            sendFile(file);
        }
    }

    private void sendFile(File file) {
        if (file.isCreateType()) {
            SendFileTask sendFileTask = new SendFileTask(file);
            clientData.getServerUtils().sendTask(sendFileTask);
        } else if (file.isDeleteType()) {
            DeleteFileTask deleteFileTask = new DeleteFileTask(file.getName());
            clientData.getServerUtils().sendTask(deleteFileTask);
        }
    }
}
