package common.model;

public enum ServerResponse {

    HI("Hi!"),
    SUCCESSFUL_SIGN_IN("User signed in successfully"),
    SUCCESSFUL_SIGN_UP("User signed up successfully"),
    FAILED_TO_SIGN_IN("Couldn't sign in"),
    FAILED_TO_SIGN_UP("Couldn't sign up"),
    FAILED_TO_SEND_FILE("Couldn't create file on server"),
    FILE_SEND_SUCCESSFULLY("File sent to server successfully");

    private String message;

    ServerResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
