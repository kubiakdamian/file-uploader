package server.client;

import common.exception.FileUploaderException;
import common.model.serverResponse.FilesOnServerResponse;
import common.model.serverResponse.ServerResponse;
import common.model.serverResponse.SignInServerResponse;
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
    private String clientName;

    public ClientUtils(Socket clientSocket) throws IOException {
        this.output = new ObjectOutputStream(clientSocket.getOutputStream());
        this.input = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void sendResponse(ServerResponse response) {
        try {
            output.writeObject(response);
            output.flush();
        } catch (IOException e) {
            throw new FileUploaderException("Couldn't send response to client");
        }
    }

    public void sendSuccessfulSigningInResponse(SignInServerResponse signInServerResponse) {
        try {
            output.writeObject(signInServerResponse);
            output.flush();
        } catch (IOException e) {
            throw new FileUploaderException("Couldn't send response to client");
        }
    }

    public void sendFilesOnServerResponse(FilesOnServerResponse filesOnServerResponse) {
        try {
            output.writeObject(filesOnServerResponse);
            output.flush();
        } catch (IOException e) {
            throw new FileUploaderException("Couldn't send response to client");
        }
    }

    public Task fetchClientTask() {
        Task task = null;

        try {
            task = (Task) input.readObject();
        } catch (IOException | ClassNotFoundException ignored) {
        }

        return task;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
