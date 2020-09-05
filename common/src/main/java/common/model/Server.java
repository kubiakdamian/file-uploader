package common.model;

import lombok.Getter;

@Getter
public class Server {

    private final String host;
    private final int port;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public Server(String[] args) {
        this.host = args[0];
        this.port = Integer.parseInt(args[1]);
    }
}
