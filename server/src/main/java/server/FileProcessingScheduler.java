package server;

import server.client.FileToProcess;
import server.config.ServerConfig;

public class FileProcessingScheduler extends Thread {

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            processFiles();
        }
    }

    private void processFiles() {
        if (doesQueueContainFreeSpace()) {
            processFileWhenFreeSpaceInQueue();
        } else if (!doesQueueContainFreeSpace() &&
                QueuedFiles.doesCurrentlyProcessingFilesContainLowPriorityTask() &&
                QueuedFiles.doesHighPriorityTaskExist()) {
            processFileWhenQueueIsFull();
        }
    }

    private void processFileWhenFreeSpaceInQueue() {
        FileToProcess fileToProcess = QueuedFiles.fetchFileToProcess();

        if (fileToProcess == null) {
            return;
        }

        startProcessingFile(fileToProcess);
    }

    private void processFileWhenQueueIsFull() {
        FileToProcess lowPriorityTask = QueuedFiles.fetchLowPriorityTaskFromCurrentlyProcessingFiles();
        lowPriorityTask.stopProcessing();
        QueuedFiles.addStoppedFile(lowPriorityTask);

        FileToProcess fileToProcess = QueuedFiles.fetchFileToProcess();
        startProcessingFile(fileToProcess);
    }

    private boolean doesQueueContainFreeSpace() {
        return QueuedFiles.sizeOfAlreadyProcessingFiles() < ServerConfig.MAX_NUMBER_OF_PROCESSING_TASKS;
    }

    private void startProcessingFile(FileToProcess fileToProcess) {
        QueuedFiles.addCurrentlyProcessingFile(fileToProcess);
        if (fileToProcess.isStopped()) {
            fileToProcess.resumeProcessing();
        } else {
            fileToProcess.start();
        }
    }
}
