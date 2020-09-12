package server.client;

import common.exception.FileUploaderException;
import common.model.ServerResponse;
import common.model.task.Task;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Getter
public class ClientUtils {

    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public ClientUtils(Socket clientSocket) throws IOException {
        this.output = new ObjectOutputStream(clientSocket.getOutputStream());
        this.input = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void sendResponse(ServerResponse response) {
        try {
            output.writeObject(response);
            output.flush();
        } catch (IOException e) {
            throw new FileUploaderException("Couldn't send task to server");
        }
    }

    public Task fetchClientTask() {
        Task task;

        try {
            task = (Task) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new FileUploaderException("Couldn't fetch server response");
        }

        return task;
    }
}
