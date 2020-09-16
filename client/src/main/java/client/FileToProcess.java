package client;

import common.model.Dictionary;
import common.model.task.FilesSynchronizedTask;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

@Getter
public class FileToProcess extends Thread {

    private final ClientData clientData;
    private final common.model.file.File fileData;

    public FileToProcess(ClientData clientData, common.model.file.File fileData) {
        this.clientData = clientData;
        this.fileData = fileData;
    }

    @Override
    public void run() {
        addFile();
    }

    private void addFile() {
        FileProcessingSimulator.process(this);
        try {
            createFileForClient();
            System.out.println("New file added: " + fileData.getName());
            QueuedFiles.removeAlreadyProcessingFile(this);

            if (QueuedFiles.isEmpty()) {
                clientData.getServerUtils().sendTask(new FilesSynchronizedTask());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createFileForClient() throws IOException {
        String clientDirectoryName = clientData.getDirectoryName();
        String filePathToCreate = Dictionary.CLIENTS_DIRECTORY + "/" + clientDirectoryName + "/" + fileData.getFullName();
        File file = new File(filePathToCreate);
        file.createNewFile();
    }

}
