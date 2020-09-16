package client.file;

public class FileProcessingSimulator {

    public static void process(FileToProcess fileToProcess) {
        int initialTime = fileToProcess.getFileData().getSize();
        for (int i = initialTime; i > 0; i--) {
            printStatus(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getLocalizedMessage());
            }
        }
    }

    private static void printStatus(int secondsLeft) {
        System.out.println("Adding file - " + secondsLeft + " second left");
    }
}
