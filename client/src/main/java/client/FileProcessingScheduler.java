package client;

import client.config.ClientConfig;

public class FileProcessingScheduler extends Thread {

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            processFiles();
        }
    }

    private void processFiles() {
        if (doesQueueContainFreeSpace()) {
            FileToProcess fileToProcess = QueuedFiles.fetchFileToProcess();

            if (fileToProcess == null) {
                return;
            }

            startProcessingFile(fileToProcess);
        }
    }

    private boolean doesQueueContainFreeSpace() {
        return QueuedFiles.sizeOfAlreadyProcessingFiles() < ClientConfig.MAX_NUMBER_OF_PROCESSING_FILES;
    }

    private void startProcessingFile(FileToProcess fileToProcess) {
        QueuedFiles.addCurrentlyProcessingFile(fileToProcess);
        fileToProcess.start();
    }
}
