package server;

import common.model.Dictionary;
import server.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerInstance {

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public void start(String[] args) throws IOException {
        if (args.length < 1) return;
        int port = Integer.parseInt(args[0]);
        createFolderForFiles();

        ServerSocket server = new ServerSocket(port);
        ServerData serverData = new ServerData();

        System.out.println("Server running...");

        while (true) {
            Socket client = server.accept();
            serverData.newClientConnected();
            ClientHandler clientHandler = new ClientHandler(client, serverData);
            pool.execute(clientHandler);
        }
    }

    private void createFolderForFiles() throws IOException {
        if (Files.notExists(Paths.get(Dictionary.DATABASE_PATH))) {
            Files.createDirectory(Paths.get(Dictionary.DATABASE_PATH));
        }
    }
}
