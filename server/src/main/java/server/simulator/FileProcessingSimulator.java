package server.simulator;

import common.model.task.TaskTypeFromClient;
import server.client.FileToProcess;

public class FileProcessingSimulator {

    public static void process(FileToProcess fileToProcess) {
        int initialTime = fileToProcess.getTask().getFile().getSize();
        for (int i = initialTime; i > 0; i--) {
            printStatus(fileToProcess, i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getLocalizedMessage());
            }

            synchronized (fileToProcess) {
                while (fileToProcess.isStopped()) {
                    try {
                        fileToProcess.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void printStatus(FileToProcess fileToProcess, int secondsLeft) {
        if (fileToProcess.getTask().getTaskType() == TaskTypeFromClient.ADD_FILE) {
            System.out.println("Adding file - " + secondsLeft + " second left");
        } else if (fileToProcess.getTask().getTaskType() == TaskTypeFromClient.DELETE_FILE) {
            System.out.println("Deleting file - " + secondsLeft + " second left");
        }
    }
}
