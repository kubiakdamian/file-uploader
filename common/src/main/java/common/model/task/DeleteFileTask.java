package common.model.task;

import common.model.file.File;

import static common.model.task.TaskTypeFromClient.DELETE_FILE;

public class DeleteFileTask extends TaskWithFile {
    public DeleteFileTask(File file) {
        super(DELETE_FILE, file);
    }
}
