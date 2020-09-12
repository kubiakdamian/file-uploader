package client;

import client.server.ServerUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.net.Socket;

@Getter
@Setter
@NoArgsConstructor
public class ClientData {

    private String name;
    private String directoryName;
    private ServerUtils serverUtils;

    public ClientData(Socket serverSocket) throws IOException {
        this.serverUtils = new ServerUtils(serverSocket);
    }
}
