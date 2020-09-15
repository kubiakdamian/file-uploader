package server;

import server.client.FileToProcess;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FilesToProcess {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Queue<FileToProcess> files = new ConcurrentLinkedQueue<>();

    public void addFile(FileToProcess file) {
        writeLock();
        files.add(file);
        writeUnlock();
    }

    public void removeFile(FileToProcess file) {
        writeLock();
        files.remove(file);
        writeUnlock();
    }

    public FileToProcess fetchFile() {
        readLock();
        FileToProcess fileToProcess = files.poll();
        readUnlock();

        return fileToProcess;
    }

    public FileToProcess fetchLowPriorityFile() {
        readLock();
        Optional<FileToProcess> fileToProcess = files.stream().filter(file -> !file.getTask().isFilePriorityHigh()).findFirst();
        fileToProcess.ifPresent(files::remove);
        readUnlock();

        return fileToProcess.get();
    }

    public boolean doesContainLowPriorityTask() {
        return files.stream().anyMatch(file -> !file.getTask().isFilePriorityHigh());
    }

    public int size() {
        return files.size();
    }

    private void writeLock() {
        lock.writeLock().lock();
    }

    private void writeUnlock() {
        lock.writeLock().unlock();
    }

    private void readLock() {
        lock.readLock().lock();
    }

    private void readUnlock() {
        lock.readLock().unlock();
    }
}
