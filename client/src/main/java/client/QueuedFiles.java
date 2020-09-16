package client;

import client.file.FileToProcess;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueuedFiles {

    private static final Queue<FileToProcess> filesToProcess = new ConcurrentLinkedQueue<>();
    private static final Queue<FileToProcess> currentlyProcessingFiles = new ConcurrentLinkedQueue<>();

    public static void addFileToProcess(FileToProcess fileToProcess) {
        filesToProcess.add(fileToProcess);
    }

    public static void addCurrentlyProcessingFile(FileToProcess fileToProcess) {
        currentlyProcessingFiles.add(fileToProcess);
    }

    public static void removeAlreadyProcessingFile(FileToProcess fileToProcess) {
        currentlyProcessingFiles.remove(fileToProcess);
    }

    public static FileToProcess fetchFileToProcess() {
        return filesToProcess.poll();
    }

    public static int sizeOfAlreadyProcessingFiles() {
        return currentlyProcessingFiles.size();
    }

    public static boolean isEmpty() {
        return filesToProcess.size() == 0 && currentlyProcessingFiles.size() == 0;

    }
}
