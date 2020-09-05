package common.exception;

public class FileUploaderException extends RuntimeException {

    public FileUploaderException() {
        super("Something went wrong");
    }

    public FileUploaderException(String message) {
        super(message);
    }

    public FileUploaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
