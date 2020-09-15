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

//        if(fileToProcess.getTask().isFilePriorityHigh()) {
//            if(QueuedFiles.doesCurrentlyProcessingFilesContainLowPriorityTask()) {
//                if(QueuedFiles.sizeOfAlreadyProcessingFiles() <= ServerConfig.MAX_NUMBER_OF_PROCESSING_TASKS) {
//
//                }
//            }
//        }

        if (QueuedFiles.sizeOfAlreadyProcessingFiles() <= ServerConfig.MAX_NUMBER_OF_PROCESSING_TASKS) {
            FileToProcess fileToProcess = QueuedFiles.getFilesToProcess().fetchFile();
            if (fileToProcess == null) {
                return;
            }

            QueuedFiles.addAlreadyProcessingFile(fileToProcess);
            fileToProcess.start();
        }
    }
}
