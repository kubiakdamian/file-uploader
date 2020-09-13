package client.service;

import client.ClientData;
import client.FilesToSend;
import common.model.Dictionary;
import common.model.file.File;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

public class FileService extends Thread {

    private final ClientData clientData;

    public FileService(ClientData clientData) {
        this.clientData = clientData;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path path = Paths.get(Dictionary.CLIENTS_DIRECTORY + "/" + clientData.getDirectoryName());
                path.register(watchService, ENTRY_CREATE, ENTRY_DELETE);
                boolean pool = true;
                while (pool) {
                    WatchKey watchKey = watchService.take();
                    for (WatchEvent<?> event : watchKey.pollEvents()) {
                        if (event.context().toString().contains("#")) {
                            System.out.println(event.kind() + " - File : " + event.context());
                            processFile(event);
                        }
                    }
                    pool = watchKey.reset();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processFile(WatchEvent<?> event) {
        String eventType = event.kind().toString();
        String clientFile = event.context().toString();
        clientFile = clientFile.replace(".txt", "");
        String[] fileParts = clientFile.split("#");

        File file = new File(fileParts[0], Integer.parseInt(fileParts[1]));

        if (eventType.equals("ENTRY_CREATE")) {
            file.setType(File.Type.CREATE);
            FilesToSend.addFile(file);
        } else if (eventType.equals("ENTRY_DELETE")) {
            file.setType(File.Type.DELETE);
            FilesToSend.addFile(file);
        }
    }
}
