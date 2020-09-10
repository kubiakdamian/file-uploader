package client.service;

import common.model.ClientData;
import common.model.Dictionary;

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
                Path path = Paths.get(Dictionary.DATABASE_PATH + "/" + clientData.getDirectoryName());
                path.register(watchService, ENTRY_CREATE, ENTRY_DELETE);
                boolean pool = true;
                while (pool) {
                    WatchKey watchKey = watchService.take();
                    for (WatchEvent<?> event : watchKey.pollEvents()) {
                        System.out.println("Event kind : " + event.kind() + " - File : " + event.context());
                    }
                    pool = watchKey.reset();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
