package server;

import server.client.FileToProcess;

public class QueuedFiles {

    private static final FilesToProcess highPriorityFiles = new FilesToProcess();

    private static final FilesToProcess lowPriorityFiles = new FilesToProcess();

    private static final FilesToProcess stoppedFiles = new FilesToProcess();

    private static final FilesToProcess currentlyProcessingFiles = new FilesToProcess();

    public static void addFileToProcess(FileToProcess fileToProcess) {
        if (fileToProcess.getTask().isFilePriorityHigh()) {
            highPriorityFiles.addFile(fileToProcess);
        } else if (!fileToProcess.getTask().isFilePriorityHigh()) {
            lowPriorityFiles.addFile(fileToProcess);
        }
    }

    public static FileToProcess fetchFileToProcess() {
        if (highPriorityFiles.size() > 0) {
            return highPriorityFiles.fetchFile();
        } else if (stoppedFiles.size() > 0) {
            return stoppedFiles.fetchFile();
        } else if (lowPriorityFiles.size() > 0) {
            return lowPriorityFiles.fetchFile();
        }

        return null;
    }

    public static void addCurrentlyProcessingFile(FileToProcess fileToProcess) {
        currentlyProcessingFiles.addFile(fileToProcess);
    }

    public static void addStoppedFile(FileToProcess fileToProcess) {
        stoppedFiles.addFile(fileToProcess);
    }

    public static void removeAlreadyProcessingFile(FileToProcess fileToProcess) {
        currentlyProcessingFiles.removeFile(fileToProcess);
    }

    public static FileToProcess fetchLowPriorityTaskFromCurrentlyProcessingFiles() {
        return currentlyProcessingFiles.fetchLowPriorityFile();
    }

    public static int sizeOfAlreadyProcessingFiles() {
        return currentlyProcessingFiles.size();
    }

    public static boolean doesCurrentlyProcessingFilesContainLowPriorityTask() {
        return currentlyProcessingFiles.doesContainLowPriorityTask();
    }

    public static boolean doesHighPriorityTaskExist() {
        return highPriorityFiles.size() > 0;
    }
}
