package server.config;

public class ServerConfig {

    public static final int WORKING_THREADS = 5;
    public static final int MAX_THREADS = 8;

    public static int getFreeThreads() {
        return MAX_THREADS - WORKING_THREADS;
    }
}
