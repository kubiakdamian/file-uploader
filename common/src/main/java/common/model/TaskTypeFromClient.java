package common.model;

public enum TaskTypeFromClient {

    CLIENT_ENTRANCE("CLIENT_ENTRANCE|"),
    ADD_FILE("ADD_FILE|"),
    DELETE_FILE("DELETE_FILE|"),
    CREATE_USER("CREATE_USER|"),
    SIGN_IN_USER("SIGN_IN_USER|"),
    GET_FILES("GET_FILES|");

    private String taskPrefix;

    TaskTypeFromClient(String taskPrefix) {
        this.taskPrefix = taskPrefix;
    }

    public String getTaskPrefix() {
        return taskPrefix;
    }
}
