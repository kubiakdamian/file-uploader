package client;

import common.model.file.File;

import java.util.LinkedList;
import java.util.Queue;

public class FilesToSend {

    private static final Queue<File> files = new LinkedList<>();

    public static void addFile(File file) {
        files.add(file);
    }

    public static int getFilesSize() {
        return files.size();
    }

    public static File getFirstFileToSend() {
        return files.poll();
    }
}
