package client;

import client.service.FileSender;
import client.service.FileService;

public class ClientListener {

    FileService fileService;
    FileSender fileSender;

    public void start(ClientData clientData) {
        this.fileService = new FileService(clientData);
        this.fileSender = new FileSender(clientData);

        fileService.start();
        fileSender.start();
    }

    public void stop() {
        fileService.interrupt();
        fileSender.interrupt();

        try {
            joinListeners();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void joinListeners() throws InterruptedException {
        fileService.join();
        fileSender.join();
    }
}
