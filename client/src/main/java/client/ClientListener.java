package client;

import client.service.FileService;

public class ClientListener {

    FileService fileService;

    public void start(ClientData clientData) {
        this.fileService = new FileService(clientData);

        fileService.start();
    }

    public void stop() {
        fileService.interrupt();

        try {
            joinListeners();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void joinListeners() throws InterruptedException {
        fileService.join();
    }
}
