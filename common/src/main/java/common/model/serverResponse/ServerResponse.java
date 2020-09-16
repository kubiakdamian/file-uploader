package common.model.serverResponse;

public enum ServerResponse {

    ALL_FILES_SYNCHRONIZED("All files synchronized"),
    FAILED_TO_SIGN_IN("Couldn't sign in"),
    FAILED_TO_SIGN_UP("Couldn't sign up"),
    FAILED_TO_SEND_FILE("Couldn't create file on server"),
    FILE_SENT_SUCCESSFULLY("File sent to server successfully"),
    FILE_DELETED_SUCCESSFULLY("File deleted from server successfully"),
    HI("Hi!"),
    SUCCESSFUL_SIGN_UP("User signed up successfully"),
    SUCCESSFUL_SIGN_IN("User signed in successfully");

    private final String message;

    ServerResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
