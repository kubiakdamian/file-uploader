package client.config;

import lombok.Getter;
import common.model.Priority;

@Getter
public class ClientConfig {

    private static ClientConfig instance = null;

    private final String name;
    private final String path;
    private final Priority priority;
    private final int serverPort;
    private final String serverIp;

    public static ClientConfig getInstance() {
        if (instance == null) {
            instance = new ClientConfig();
        }

        return instance;
    }

    private ClientConfig() {
        this.name = System.getenv("client.name");
        this.path = System.getenv("client.path");
        this.priority = Priority.valueOf(System.getenv("client.priority"));
        this.serverPort = Integer.parseInt(System.getenv("server.port"));
        this.serverIp = System.getenv("server.ip");
    }
}
