package client.server;

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
public class ServerUtils {

    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public ServerUtils(Socket serverSocket) throws IOException {
        this.output = new ObjectOutputStream(serverSocket.getOutputStream());
        this.input = new ObjectInputStream(serverSocket.getInputStream());
    }

    public void sendTask(Task task) {
        try {
            output.writeObject(task);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileUploaderException("Couldn't send task to server");
        }
    }

    public ServerResponse fetchServerResponse() {
        ServerResponse serverResponse;

        try {
            serverResponse = (ServerResponse) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new FileUploaderException("Couldn't fetch server response");
        }

        return serverResponse;
    }

    public SignInServerResponse fetchSignInServerResponse() {
        SignInServerResponse signInServerResponse;

        try {
            signInServerResponse = (SignInServerResponse) input.readObject();
        } catch (ClassCastException classCastException) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            throw new FileUploaderException("Couldn't fetch server response");
        }

        return signInServerResponse;
    }

    public FilesOnServerResponse fetchFilesFromServer() {
        FilesOnServerResponse filesOnServerResponse;

        try {
            filesOnServerResponse = (FilesOnServerResponse) input.readObject();
        } catch (ClassCastException classCastException) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            throw new FileUploaderException("Couldn't fetch server response");
        }

        return filesOnServerResponse;
    }

    public void closeConnection() {
        try {
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
