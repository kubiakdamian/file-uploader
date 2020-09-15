package server;

import lombok.Getter;
import server.client.FileToProcess;

@Getter
public class QueuedFiles {

    private static final FilesToProcess filesToProcess = new FilesToProcess();

    private static final FilesToProcess currentlyProcessingFiles = new FilesToProcess();

    public static void addFileToProcess(FileToProcess fileToProcess) {
        filesToProcess.addFile(fileToProcess);
    }

    public static void removeFileToProcess(FileToProcess fileToProcess) {
        filesToProcess.removeFile(fileToProcess);
    }

    public static void addAlreadyProcessingFile(FileToProcess fileToProcess) {
        currentlyProcessingFiles.addFile(fileToProcess);
    }

    public static void removeAlreadyProcessingFile(FileToProcess fileToProcess) {
        currentlyProcessingFiles.removeFile(fileToProcess);
    }

    public static FilesToProcess getFilesToProcess() {
        return filesToProcess;
    }

    public static FilesToProcess getCurrentlyProcessingFiles() {
        return currentlyProcessingFiles;
    }

    public static int sizeOfFilesToProcess() {
        return filesToProcess.size();
    }

    public static int sizeOfAlreadyProcessingFiles() {
        return currentlyProcessingFiles.size();
    }

    public static boolean doesCurrentlyProcessingFilesContainLowPriorityTask() {
        return currentlyProcessingFiles.doesContainLowPriorityTask();
    }
}
