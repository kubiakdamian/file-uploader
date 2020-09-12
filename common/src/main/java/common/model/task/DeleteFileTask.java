package common.model.task;

import static common.model.task.TaskTypeFromClient.DELETE_FILE;

public class DeleteFileTask extends Task {

    private final String filename;

    public DeleteFileTask(String filename) {
        super(DELETE_FILE);
        this.filename = filename;
    }
}
